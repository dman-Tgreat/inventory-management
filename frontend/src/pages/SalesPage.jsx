import { useEffect, useState } from 'react'
import { Plus, Trash2 } from 'lucide-react'
import PageHeader from '../components/PageHeader'
import DataTable from '../components/DataTable'
import Modal from '../components/Modal'
import LoadingSpinner from '../components/LoadingSpinner'
import { salesApi, productsApi } from '../api/client'
import { formatCurrency, formatDate } from '../utils/format'
import { useAuth } from '../context/AuthContext'

export default function SalesPage() {
  const { user } = useAuth()
  const [sales, setSales] = useState([])
  const [products, setProducts] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [modalOpen, setModalOpen] = useState(false)
  const [items, setItems] = useState([{ productId: '', quantity: 1 }])
  const [saving, setSaving] = useState(false)

  const load = async () => {
    setLoading(true)
    try {
      const [s, p] = await Promise.all([salesApi.getAll(), productsApi.getAll()])
      setSales(s)
      setProducts(p)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { load() }, [])

  const addItem = () => setItems([...items, { productId: '', quantity: 1 }])

  const removeItem = (idx) => {
    if (items.length === 1) return
    setItems(items.filter((_, i) => i !== idx))
  }

  const updateItem = (idx, field, value) => {
    const updated = [...items]
    updated[idx] = { ...updated[idx], [field]: value }
    setItems(updated)
  }

  const handleSave = async (e) => {
    e.preventDefault()
    setSaving(true)
    try {
      await salesApi.create({
        userId: user.id,
        items: items.map((i) => ({
          productId: Number(i.productId),
          quantity: Number(i.quantity),
        })),
      })
      setModalOpen(false)
      setItems([{ productId: '', quantity: 1 }])
      load()
    } catch (err) {
      alert(err.message)
    } finally {
      setSaving(false)
    }
  }

  const inputClass =
    'w-full rounded-lg border border-surface-200 bg-white px-3 py-2 text-sm text-surface-900 focus:border-primary-500 focus:outline-none focus:ring-2 focus:ring-primary-500/20 dark:border-surface-700 dark:bg-surface-800 dark:text-white'

  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'userName', label: 'Sold By' },
    { key: 'saleDate', label: 'Date', render: (r) => formatDate(r.saleDate) },
    { key: 'totalAmount', label: 'Total', render: (r) => formatCurrency(r.totalAmount) },
    { key: 'profit', label: 'Profit', render: (r) => formatCurrency(r.profit) },
  ]

  return (
    <div>
      <PageHeader
        title="Sales"
        description="Record and track sales transactions"
        action={
          <button
            onClick={() => setModalOpen(true)}
            className="inline-flex items-center gap-2 rounded-xl bg-primary-500 px-4 py-2.5 text-sm font-semibold text-white shadow-lg shadow-primary-500/25 hover:bg-primary-600"
          >
            <Plus size={16} />
            New Sale
          </button>
        }
      />

      {error && (
        <div className="mb-4 rounded-xl border border-rose-200 bg-rose-50 p-3 text-sm text-rose-600 dark:border-rose-500/30 dark:bg-rose-500/10 dark:text-rose-400">
          {error}
        </div>
      )}

      {loading ? <LoadingSpinner className="py-12" /> : <DataTable columns={columns} data={sales} />}

      <Modal open={modalOpen} onClose={() => setModalOpen(false)} title="Record Sale" wide>
        <form onSubmit={handleSave} className="space-y-4">
          <div className="space-y-3">
            <div className="flex items-center justify-between">
              <label className="text-xs font-medium text-surface-700 dark:text-surface-200">Items *</label>
              <button type="button" onClick={addItem} className="text-xs font-medium text-primary-600 hover:text-primary-700 dark:text-primary-400">
                + Add item
              </button>
            </div>
            {items.map((item, idx) => (
              <div key={idx} className="grid gap-2 rounded-lg border border-surface-200 p-3 dark:border-surface-700 sm:grid-cols-3">
                <select value={item.productId} onChange={(e) => updateItem(idx, 'productId', e.target.value)} required className={inputClass}>
                  <option value="">Product</option>
                  {products.map((p) => (
                    <option key={p.id} value={p.id}>
                      {p.name} (stock: {p.quantity})
                    </option>
                  ))}
                </select>
                <input type="number" min="1" value={item.quantity} onChange={(e) => updateItem(idx, 'quantity', e.target.value)} placeholder="Qty" required className={inputClass} />
                <button type="button" onClick={() => removeItem(idx)} className="flex items-center justify-center rounded-lg text-rose-600 hover:bg-rose-50 dark:text-rose-400 dark:hover:bg-rose-500/10">
                  <Trash2 size={16} />
                </button>
              </div>
            ))}
          </div>

          <div className="flex justify-end gap-2 pt-2">
            <button type="button" onClick={() => setModalOpen(false)} className="rounded-lg px-4 py-2 text-sm text-surface-700 hover:bg-surface-100 dark:text-surface-200 dark:hover:bg-surface-800">
              Cancel
            </button>
            <button type="submit" disabled={saving} className="rounded-lg bg-primary-500 px-4 py-2 text-sm font-semibold text-white hover:bg-primary-600 disabled:opacity-50">
              {saving ? 'Saving...' : 'Record Sale'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  )
}
