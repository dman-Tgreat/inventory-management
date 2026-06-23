import { useEffect, useState } from 'react'
import { Plus, Trash2 } from 'lucide-react'
import PageHeader from '../components/PageHeader'
import DataTable from '../components/DataTable'
import Modal from '../components/Modal'
import LoadingSpinner from '../components/LoadingSpinner'
import { purchasesApi, suppliersApi, productsApi } from '../api/client'
import { formatCurrency, formatDate } from '../utils/format'

export default function PurchasesPage() {
  const [purchases, setPurchases] = useState([])
  const [suppliers, setSuppliers] = useState([])
  const [products, setProducts] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [modalOpen, setModalOpen] = useState(false)
  const [supplierId, setSupplierId] = useState('')
  const [items, setItems] = useState([{ productId: '', quantity: 1, unitCost: '' }])
  const [saving, setSaving] = useState(false)

  const load = async () => {
    setLoading(true)
    try {
      const [p, s, pr] = await Promise.all([
        purchasesApi.getAll(),
        suppliersApi.getAll(),
        productsApi.getAll(),
      ])
      setPurchases(p)
      setSuppliers(s)
      setProducts(pr)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { load() }, [])

  const addItem = () => setItems([...items, { productId: '', quantity: 1, unitCost: '' }])

  const removeItem = (idx) => {
    if (items.length === 1) return
    setItems(items.filter((_, i) => i !== idx))
  }

  const updateItem = (idx, field, value) => {
    const updated = [...items]
    updated[idx] = { ...updated[idx], [field]: value }
    if (field === 'productId') {
      const product = products.find((p) => p.id === Number(value))
      if (product) updated[idx].unitCost = product.costPrice
    }
    setItems(updated)
  }

  const handleSave = async (e) => {
    e.preventDefault()
    setSaving(true)
    try {
      await purchasesApi.create({
        supplierId: Number(supplierId),
        items: items.map((i) => ({
          productId: Number(i.productId),
          quantity: Number(i.quantity),
          unitCost: Number(i.unitCost),
        })),
      })
      setModalOpen(false)
      setSupplierId('')
      setItems([{ productId: '', quantity: 1, unitCost: '' }])
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
    { key: 'supplierName', label: 'Supplier' },
    { key: 'purchaseDate', label: 'Date', render: (r) => formatDate(r.purchaseDate) },
    { key: 'totalAmount', label: 'Total', render: (r) => formatCurrency(r.totalAmount) },
  ]

  return (
    <div>
      <PageHeader
        title="Purchases"
        description="Record and track purchase orders"
        action={
          <button
            onClick={() => setModalOpen(true)}
            className="inline-flex items-center gap-2 rounded-xl bg-primary-500 px-4 py-2.5 text-sm font-semibold text-white shadow-lg shadow-primary-500/25 hover:bg-primary-600"
          >
            <Plus size={16} />
            New Purchase
          </button>
        }
      />

      {error && (
        <div className="mb-4 rounded-xl border border-rose-200 bg-rose-50 p-3 text-sm text-rose-600 dark:border-rose-500/30 dark:bg-rose-500/10 dark:text-rose-400">
          {error}
        </div>
      )}

      {loading ? <LoadingSpinner className="py-12" /> : <DataTable columns={columns} data={purchases} />}

      <Modal open={modalOpen} onClose={() => setModalOpen(false)} title="Record Purchase" wide>
        <form onSubmit={handleSave} className="space-y-4">
          <div>
            <label className="mb-1 block text-xs font-medium text-surface-700 dark:text-surface-200">Supplier *</label>
            <select value={supplierId} onChange={(e) => setSupplierId(e.target.value)} required className={inputClass}>
              <option value="">Select supplier</option>
              {suppliers.map((s) => <option key={s.id} value={s.id}>{s.name}</option>)}
            </select>
          </div>

          <div className="space-y-3">
            <div className="flex items-center justify-between">
              <label className="text-xs font-medium text-surface-700 dark:text-surface-200">Items *</label>
              <button type="button" onClick={addItem} className="text-xs font-medium text-primary-600 hover:text-primary-700 dark:text-primary-400">
                + Add item
              </button>
            </div>
            {items.map((item, idx) => (
              <div key={idx} className="grid gap-2 rounded-lg border border-surface-200 p-3 dark:border-surface-700 sm:grid-cols-4">
                <select value={item.productId} onChange={(e) => updateItem(idx, 'productId', e.target.value)} required className={inputClass}>
                  <option value="">Product</option>
                  {products.map((p) => <option key={p.id} value={p.id}>{p.name}</option>)}
                </select>
                <input type="number" min="1" value={item.quantity} onChange={(e) => updateItem(idx, 'quantity', e.target.value)} placeholder="Qty" required className={inputClass} />
                <input type="number" step="0.01" min="0" value={item.unitCost} onChange={(e) => updateItem(idx, 'unitCost', e.target.value)} placeholder="Unit cost" required className={inputClass} />
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
              {saving ? 'Saving...' : 'Record Purchase'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  )
}
