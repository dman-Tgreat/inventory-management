import { useEffect, useState } from 'react'
import {
  Package,
  Tags,
  Truck,
  DollarSign,
  TrendingUp,
  AlertTriangle,
  ShoppingCart,
  Receipt,
} from 'lucide-react'
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
  Legend,
} from 'recharts'
import PageHeader from '../components/PageHeader'
import StatCard from '../components/StatCard'
import LoadingSpinner from '../components/LoadingSpinner'
import { dashboardApi, inventoryApi } from '../api/client'
import { formatCurrency, formatNumber } from '../utils/format'

const CHART_COLORS = ['#6366f1', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6']

function FinancialTooltip({ active, payload, label }) {
  if (!active || !payload?.length) return null
  return (
    <div className="rounded-lg border border-surface-200 bg-white px-3 py-2 shadow-lg dark:border-surface-700 dark:bg-surface-800">
      <p className="text-xs font-medium text-surface-700 dark:text-surface-200">{label}</p>
      <p className="text-sm font-bold text-surface-900 dark:text-white">
        {formatCurrency(payload[0].value)}
      </p>
    </div>
  )
}

export default function DashboardPage() {
  const [dashboard, setDashboard] = useState(null)
  const [inventory, setInventory] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    Promise.all([dashboardApi.get(), inventoryApi.getSummary()])
      .then(([dash, inv]) => {
        setDashboard(dash)
        setInventory(inv)
      })
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <LoadingSpinner size="lg" className="py-20" />
  if (error) {
    return (
      <div className="rounded-xl border border-rose-200 bg-rose-50 p-4 text-sm text-rose-600 dark:border-rose-500/30 dark:bg-rose-500/10 dark:text-rose-400">
        {error}
      </div>
    )
  }

  const financialData = [
    { name: "Today's Sales", value: Number(dashboard.todaysSales) },
    { name: "Today's Purchases", value: Number(dashboard.todaysPurchases) },
    { name: 'Revenue', value: Number(dashboard.revenue) },
    { name: 'Profit', value: Number(dashboard.profit) },
  ]

  const stockData = [
    { name: 'In Stock', value: inventory.currentStock - inventory.lowStockProducts - inventory.outOfStockProducts },
    { name: 'Low Stock', value: inventory.lowStockProducts },
    { name: 'Out of Stock', value: inventory.outOfStockProducts },
  ].filter((d) => d.value > 0)

  return (
    <div>
      <PageHeader
        title="Dashboard"
        description="Overview of your inventory and business performance"
      />

      <div className="mb-6 grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
        <StatCard title="Total Products" value={formatNumber(dashboard.totalProducts)} icon={Package} color="primary" />
        <StatCard title="Categories" value={formatNumber(dashboard.totalCategories)} icon={Tags} color="violet" />
        <StatCard title="Suppliers" value={formatNumber(dashboard.totalSuppliers)} icon={Truck} color="cyan" />
        <StatCard
          title="Low Stock Items"
          value={formatNumber(dashboard.lowStock)}
          icon={AlertTriangle}
          color="amber"
          subtitle="Needs attention"
        />
      </div>

      <div className="mb-6 grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
        <StatCard title="Today's Sales" value={formatCurrency(dashboard.todaysSales)} icon={Receipt} color="emerald" />
        <StatCard title="Today's Purchases" value={formatCurrency(dashboard.todaysPurchases)} icon={ShoppingCart} color="rose" />
        <StatCard title="Total Revenue" value={formatCurrency(dashboard.revenue)} icon={DollarSign} color="primary" />
        <StatCard title="Total Profit" value={formatCurrency(dashboard.profit)} icon={TrendingUp} color="emerald" />
      </div>

      <div className="grid gap-6 lg:grid-cols-2">
        <div className="rounded-2xl border border-surface-200 bg-white p-5 shadow-sm dark:border-surface-700 dark:bg-surface-800">
          <h3 className="mb-4 text-sm font-semibold text-surface-900 dark:text-white">Financial Overview</h3>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={financialData} barSize={40}>
              <CartesianGrid strokeDasharray="3 3" stroke="#334155" opacity={0.2} />
              <XAxis dataKey="name" tick={{ fontSize: 11 }} stroke="#94a3b8" />
              <YAxis tick={{ fontSize: 11 }} stroke="#94a3b8" tickFormatter={(v) => `$${v}`} />
              <Tooltip content={<FinancialTooltip />} />
              <Bar dataKey="value" radius={[6, 6, 0, 0]}>
                {financialData.map((_, i) => (
                  <Cell key={i} fill={CHART_COLORS[i % CHART_COLORS.length]} />
                ))}
              </Bar>
            </BarChart>
          </ResponsiveContainer>
        </div>

        <div className="rounded-2xl border border-surface-200 bg-white p-5 shadow-sm dark:border-surface-700 dark:bg-surface-800">
          <h3 className="mb-4 text-sm font-semibold text-surface-900 dark:text-white">Stock Distribution</h3>
          {stockData.length > 0 ? (
            <ResponsiveContainer width="100%" height={300}>
              <PieChart>
                <Pie
                  data={stockData}
                  cx="50%"
                  cy="50%"
                  innerRadius={60}
                  outerRadius={100}
                  paddingAngle={4}
                  dataKey="value"
                >
                  {stockData.map((_, i) => (
                    <Cell key={i} fill={CHART_COLORS[i % CHART_COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip formatter={(v) => formatNumber(v)} />
                <Legend />
              </PieChart>
            </ResponsiveContainer>
          ) : (
            <div className="flex h-[300px] items-center justify-center text-sm text-surface-700 dark:text-surface-200">
              No stock data available
            </div>
          )}
        </div>
      </div>

      <div className="mt-6 rounded-2xl border border-surface-200 bg-white p-5 shadow-sm dark:border-surface-700 dark:bg-surface-800">
        <h3 className="mb-3 text-sm font-semibold text-surface-900 dark:text-white">Inventory Value</h3>
        <p className="text-3xl font-bold text-primary-600 dark:text-primary-400">
          {formatCurrency(inventory.inventoryValue)}
        </p>
        <p className="mt-1 text-sm text-surface-700 dark:text-surface-200">
          {formatNumber(inventory.totalProducts)} products · {formatNumber(inventory.currentStock)} total units
        </p>
      </div>
    </div>
  )
}
