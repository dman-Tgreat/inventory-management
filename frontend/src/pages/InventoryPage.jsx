import { useEffect, useState } from 'react'
import { Package, AlertTriangle, XCircle, DollarSign } from 'lucide-react'
import PageHeader from '../components/PageHeader'
import StatCard from '../components/StatCard'
import DataTable from '../components/DataTable'
import LoadingSpinner from '../components/LoadingSpinner'
import { inventoryApi } from '../api/client'
import { formatCurrency, formatNumber } from '../utils/format'

const TABS = [
  { id: 'summary', label: 'Overview' },
  { id: 'low', label: 'Low Stock' },
  { id: 'out', label: 'Out of Stock' },
]

export default function InventoryPage() {
  const [tab, setTab] = useState('summary')
  const [summary, setSummary] = useState(null)
  const [lowStock, setLowStock] = useState([])
  const [outOfStock, setOutOfStock] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    setLoading(true)
    Promise.all([
      inventoryApi.getSummary(),
      inventoryApi.getLowStock(),
      inventoryApi.getOutOfStock(),
    ])
      .then(([s, low, out]) => {
        setSummary(s)
        setLowStock(low)
        setOutOfStock(out)
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false))
  }, [])

  const productColumns = [
    { key: 'sku', label: 'SKU' },
    { key: 'name', label: 'Name' },
    { key: 'quantity', label: 'Quantity' },
    { key: 'minimumStock', label: 'Min Stock' },
    {
      key: 'sellingPrice',
      label: 'Price',
      render: (r) => formatCurrency(r.sellingPrice),
    },
  ]

  if (loading) return <LoadingSpinner size="lg" className="py-20" />

  return (
    <div>
      <PageHeader title="Inventory" description="Monitor stock levels and inventory health" />

      {error && (
        <div className="mb-4 rounded-xl border border-rose-200 bg-rose-50 p-3 text-sm text-rose-600 dark:border-rose-500/30 dark:bg-rose-500/10 dark:text-rose-400">
          {error}
        </div>
      )}

      {summary && (
        <div className="mb-6 grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
          <StatCard title="Total Products" value={formatNumber(summary.totalProducts)} icon={Package} color="primary" />
          <StatCard title="Current Stock" value={formatNumber(summary.currentStock)} icon={Package} color="cyan" />
          <StatCard title="Low Stock" value={formatNumber(summary.lowStockProducts)} icon={AlertTriangle} color="amber" />
          <StatCard title="Inventory Value" value={formatCurrency(summary.inventoryValue)} icon={DollarSign} color="emerald" />
        </div>
      )}

      <div className="mb-4 flex gap-1 rounded-xl border border-surface-200 bg-surface-100 p-1 dark:border-surface-700 dark:bg-surface-800">
        {TABS.map((t) => (
          <button
            key={t.id}
            onClick={() => setTab(t.id)}
            className={`flex-1 rounded-lg px-3 py-2 text-sm font-medium transition-all ${
              tab === t.id
                ? 'bg-white text-primary-600 shadow-sm dark:bg-surface-900 dark:text-primary-400'
                : 'text-surface-700 hover:text-surface-900 dark:text-surface-200 dark:hover:text-white'
            }`}
          >
            {t.label}
            {t.id === 'low' && summary && summary.lowStockProducts > 0 && (
              <span className="ml-1.5 rounded-full bg-amber-500/20 px-1.5 py-0.5 text-xs text-amber-600 dark:text-amber-400">
                {summary.lowStockProducts}
              </span>
            )}
            {t.id === 'out' && summary && summary.outOfStockProducts > 0 && (
              <span className="ml-1.5 rounded-full bg-rose-500/20 px-1.5 py-0.5 text-xs text-rose-600 dark:text-rose-400">
                {summary.outOfStockProducts}
              </span>
            )}
          </button>
        ))}
      </div>

      {tab === 'summary' && summary && (
        <div className="grid gap-4 sm:grid-cols-3">
          <div className="rounded-2xl border border-surface-200 bg-white p-5 dark:border-surface-700 dark:bg-surface-800">
            <div className="flex items-center gap-3">
              <div className="rounded-xl bg-emerald-500/10 p-2.5 text-emerald-600 dark:text-emerald-400">
                <Package size={20} />
              </div>
              <div>
                <p className="text-sm text-surface-700 dark:text-surface-200">Healthy Stock</p>
                <p className="text-xl font-bold text-surface-900 dark:text-white">
                  {formatNumber(summary.currentStock - summary.lowStockProducts - summary.outOfStockProducts)}
                </p>
              </div>
            </div>
          </div>
          <div className="rounded-2xl border border-amber-200 bg-amber-50 p-5 dark:border-amber-500/30 dark:bg-amber-500/10">
            <div className="flex items-center gap-3">
              <div className="rounded-xl bg-amber-500/20 p-2.5 text-amber-600 dark:text-amber-400">
                <AlertTriangle size={20} />
              </div>
              <div>
                <p className="text-sm text-amber-700 dark:text-amber-300">Low Stock Items</p>
                <p className="text-xl font-bold text-amber-900 dark:text-amber-200">{formatNumber(summary.lowStockProducts)}</p>
              </div>
            </div>
          </div>
          <div className="rounded-2xl border border-rose-200 bg-rose-50 p-5 dark:border-rose-500/30 dark:bg-rose-500/10">
            <div className="flex items-center gap-3">
              <div className="rounded-xl bg-rose-500/20 p-2.5 text-rose-600 dark:text-rose-400">
                <XCircle size={20} />
              </div>
              <div>
                <p className="text-sm text-rose-700 dark:text-rose-300">Out of Stock</p>
                <p className="text-xl font-bold text-rose-900 dark:text-rose-200">{formatNumber(summary.outOfStockProducts)}</p>
              </div>
            </div>
          </div>
        </div>
      )}

      {tab === 'low' && <DataTable columns={productColumns} data={lowStock} emptyMessage="No low stock items" />}
      {tab === 'out' && <DataTable columns={productColumns} data={outOfStock} emptyMessage="No out of stock items" />}
    </div>
  )
}
