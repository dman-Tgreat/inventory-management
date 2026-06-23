import { useEffect, useState, useCallback } from 'react'
import { Plus, Search, Pencil, Trash2, Filter } from 'lucide-react'
import PageHeader from '../components/PageHeader'
import DataTable from '../components/DataTable'
import Modal from '../components/Modal'
import LoadingSpinner from '../components/LoadingSpinner'
import { productsApi, categoriesApi, suppliersApi } from '../api/client'
import { formatCurrency, formatDate } from '../utils/format'
import { useAuth } from '../context/AuthContext'

const emptyProduct = {
  sku: '',
  name: '',
  description: '',
  categoryId: '',
  supplierId: '',
  costPrice: '',
  sellingPrice: '',
  quantity: 0,
  minimumStock: 0,
  barcode: '',
}

export default function ProductsPage() {
  const { isAdmin } = useAuth()
  const [products, setProducts] = useState([])
  const [categories, setCategories] = useState([])
  const [suppliers, setSuppliers] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [search, setSearch] = useState('')
  const [filterCategory, setFilterCategory] = useState('')
  const [filterSupplier, setFilterSupplier] = useState('')
  const [lowStockOnly, setLowStockOnly] = useState(false)
  const [modalOpen, setModalOpen] = useState(false)
  const [editing, setEditing] = useState(null)
  const [form, setForm] = useState(emptyProduct)
  const [saving, setSaving] = useState(false)

  const loadData = useCallback(async () => {
    setLoading(true)
    setError('')
    try {
      const [cats, sups] = await Promise.all([categoriesApi.getAll(), suppliersApi.getAll()])
      setCategories(cats)
      setSuppliers(sups)

      let prods
      if (search.trim()) {
        prods = await productsApi.search(search.trim())
      } else if (filterCategory || filterSupplier || lowStockOnly) {
        prods = await productsApi.filter({
          categoryId: filterCategory || undefined,
          supplierId: filterSupplier || undefined,
          lowStock: lowStockOnly || undefined,
        })
      } else {
        prods = await productsApi.getAll()
      }
      setProducts(prods)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }, [search, filterCategory, filterSupplier, lowStockOnly])

  useEffect(() => {
    loadData()
  }, [loadData])

  const openCreate = () => {
    setEditing(null)
    setForm(emptyProduct)
    setModalOpen(true)
  }

  const openEdit = (product) => {
    setEditing(product)
    setForm({
      sku: product.sku,
      name: product.name,
      description: product.description || '',
      categoryId: product.categoryId || '',
      supplierId: product.supplierId || '',
      costPrice: product.costPrice,
      sellingPrice: product.sellingPrice,
      quantity: product.quantity,
      minimumStock: product.minimumStock,
      barcode: product.barcode || '',
    })
    setModalOpen(true)
  }

  const handleSave = async (e) => {
    e.preventDefault()
    setSaving(true)
    try {
      const payload = {
        ...form,
        categoryId: form.categoryId ? Number(form.categoryId) : null,
        supplierId: form.supplierId ? Number(form.supplierId) : null,
        costPrice: Number(form.costPrice),
        sellingPrice: Number(form.sellingPrice),
        quantity: Number(form.quantity),
        minimumStock: Number(form.minimumStock),
      }
      if (editing) {
        await productsApi.update(editing.id, payload)
      } else {
        await productsApi.create(payload)
      }
      setModalOpen(false)
      loadData()
    } catch (err) {
      alert(err.message)
    } finally {
      setSaving(false)
    }
  }

  const handleDelete = async (id) => {
    if (!confirm('Delete this product?')) return
    try {
      await productsApi.delete(id)
      loadData()
    } catch (err) {
      alert(err.message)
    }
  }

  const columns = [
    { key: 'sku', label: 'SKU' },
    { key: 'name', label: 'Name' },
    {
      key: 'costPrice',
      label: 'Cost',
      render: (r) => formatCurrency(r.costPrice),
    },
    {
      key: 'sellingPrice',
      label: 'Price',
      render: (r) => formatCurrency(r.sellingPrice),
    },
    {
      key: 'quantity',
      label: 'Qty',
      render: (r) => (
        <span
          className={
            r.quantity === 0
              ? 'font-semibold text-rose-600 dark:text-rose-400'
              : r.quantity <= r.minimumStock
                ? 'font-semibold text-amber-600 dark:text-amber-400'
                : ''
          }
        >
          {r.quantity}
        </span>
      ),
    },
    { key: 'minimumStock', label: 'Min Stock' },
    {
      key: 'createdAt',
      label: 'Created',
      render: (r) => formatDate(r.createdAt),
    },
    ...(isAdmin
      ? [
          {
            key: 'actions',
            label: 'Actions',
            render: (r) => (
              <div className="flex gap-1">
                <button
                  onClick={() => openEdit(r)}
                  className="rounded-lg p-1.5 text-primary-600 hover:bg-primary-50 dark:text-primary-400 dark:hover:bg-primary-500/10"
                >
                  <Pencil size={15} />
                </button>
                <button
                  onClick={() => handleDelete(r.id)}
                  className="rounded-lg p-1.5 text-rose-600 hover:bg-rose-50 dark:text-rose-400 dark:hover:bg-rose-500/10"
                >
                  <Trash2 size={15} />
                </button>
              </div>
            ),
          },
        ]
      : []),
  ]

  const inputClass =
    'w-full rounded-lg border border-surface-200 bg-white px-3 py-2 text-sm text-surface-900 focus:border-primary-500 focus:outline-none focus:ring-2 focus:ring-primary-500/20 dark:border-surface-700 dark:bg-surface-800 dark:text-white'

  return (
    <div>
      <PageHeader
        title="Products"
        description="Manage your product catalog"
        action={
          isAdmin && (
            <button
              onClick={openCreate}
              className="inline-flex items-center gap-2 rounded-xl bg-primary-500 px-4 py-2.5 text-sm font-semibold text-white shadow-lg shadow-primary-500/25 hover:bg-primary-600"
            >
              <Plus size={16} />
              Add Product
            </button>
          )
        }
      />

      <div className="mb-4 flex flex-col gap-3 sm:flex-row sm:flex-wrap">
        <div className="relative flex-1 sm:max-w-xs">
          <Search size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-surface-700 dark:text-surface-200" />
          <input
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            placeholder="Search by name or SKU..."
            className={`${inputClass} pl-9`}
          />
        </div>
        <select
          value={filterCategory}
          onChange={(e) => { setSearch(''); setFilterCategory(e.target.value) }}
          className={`${inputClass} sm:w-40`}
        >
          <option value="">All Categories</option>
          {categories.map((c) => (
            <option key={c.id} value={c.id}>{c.name}</option>
          ))}
        </select>
        <select
          value={filterSupplier}
          onChange={(e) => { setSearch(''); setFilterSupplier(e.target.value) }}
          className={`${inputClass} sm:w-40`}
        >
          <option value="">All Suppliers</option>
          {suppliers.map((s) => (
            <option key={s.id} value={s.id}>{s.name}</option>
          ))}
        </select>
        <label className="inline-flex items-center gap-2 text-sm text-surface-700 dark:text-surface-200">
          <input
            type="checkbox"
            checked={lowStockOnly}
            onChange={(e) => { setSearch(''); setLowStockOnly(e.target.checked) }}
            className="rounded border-surface-300 text-primary-500 focus:ring-primary-500"
          />
          <Filter size={14} />
          Low stock only
        </label>
      </div>

      {error && (
        <div className="mb-4 rounded-xl border border-rose-200 bg-rose-50 p-3 text-sm text-rose-600 dark:border-rose-500/30 dark:bg-rose-500/10 dark:text-rose-400">
          {error}
        </div>
      )}

      {loading ? <LoadingSpinner className="py-12" /> : <DataTable columns={columns} data={products} />}

      <Modal open={modalOpen} onClose={() => setModalOpen(false)} title={editing ? 'Edit Product' : 'Add Product'} wide>
        <form onSubmit={handleSave} className="space-y-4">
          <div className="grid gap-4 sm:grid-cols-2">
            <div>
              <label className="mb-1 block text-xs font-medium text-surface-700 dark:text-surface-200">SKU *</label>
              <input value={form.sku} onChange={(e) => setForm({ ...form, sku: e.target.value })} required className={inputClass} />
            </div>
            <div>
              <label className="mb-1 block text-xs font-medium text-surface-700 dark:text-surface-200">Name *</label>
              <input value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required className={inputClass} />
            </div>
            <div>
              <label className="mb-1 block text-xs font-medium text-surface-700 dark:text-surface-200">Category</label>
              <select value={form.categoryId} onChange={(e) => setForm({ ...form, categoryId: e.target.value })} className={inputClass}>
                <option value="">None</option>
                {categories.map((c) => <option key={c.id} value={c.id}>{c.name}</option>)}
              </select>
            </div>
            <div>
              <label className="mb-1 block text-xs font-medium text-surface-700 dark:text-surface-200">Supplier</label>
              <select value={form.supplierId} onChange={(e) => setForm({ ...form, supplierId: e.target.value })} className={inputClass}>
                <option value="">None</option>
                {suppliers.map((s) => <option key={s.id} value={s.id}>{s.name}</option>)}
              </select>
            </div>
            <div>
              <label className="mb-1 block text-xs font-medium text-surface-700 dark:text-surface-200">Cost Price *</label>
              <input type="number" step="0.01" min="0" value={form.costPrice} onChange={(e) => setForm({ ...form, costPrice: e.target.value })} required className={inputClass} />
            </div>
            <div>
              <label className="mb-1 block text-xs font-medium text-surface-700 dark:text-surface-200">Selling Price *</label>
              <input type="number" step="0.01" min="0" value={form.sellingPrice} onChange={(e) => setForm({ ...form, sellingPrice: e.target.value })} required className={inputClass} />
            </div>
            <div>
              <label className="mb-1 block text-xs font-medium text-surface-700 dark:text-surface-200">Quantity</label>
              <input type="number" min="0" value={form.quantity} onChange={(e) => setForm({ ...form, quantity: e.target.value })} className={inputClass} />
            </div>
            <div>
              <label className="mb-1 block text-xs font-medium text-surface-700 dark:text-surface-200">Minimum Stock</label>
              <input type="number" min="0" value={form.minimumStock} onChange={(e) => setForm({ ...form, minimumStock: e.target.value })} className={inputClass} />
            </div>
            <div className="sm:col-span-2">
              <label className="mb-1 block text-xs font-medium text-surface-700 dark:text-surface-200">Barcode</label>
              <input value={form.barcode} onChange={(e) => setForm({ ...form, barcode: e.target.value })} className={inputClass} />
            </div>
            <div className="sm:col-span-2">
              <label className="mb-1 block text-xs font-medium text-surface-700 dark:text-surface-200">Description</label>
              <textarea value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} rows={2} className={inputClass} />
            </div>
          </div>
          <div className="flex justify-end gap-2 pt-2">
            <button type="button" onClick={() => setModalOpen(false)} className="rounded-lg px-4 py-2 text-sm text-surface-700 hover:bg-surface-100 dark:text-surface-200 dark:hover:bg-surface-800">
              Cancel
            </button>
            <button type="submit" disabled={saving} className="rounded-lg bg-primary-500 px-4 py-2 text-sm font-semibold text-white hover:bg-primary-600 disabled:opacity-50">
              {saving ? 'Saving...' : editing ? 'Update' : 'Create'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  )
}
