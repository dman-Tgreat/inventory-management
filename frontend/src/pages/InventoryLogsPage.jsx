import { useEffect, useState } from 'react'
import PageHeader from '../components/PageHeader'
import DataTable from '../components/DataTable'
import LoadingSpinner from '../components/LoadingSpinner'
import { inventoryApi } from '../api/client'
import { formatDate } from '../utils/format'

export default function InventoryLogsPage() {
  const [logs, setLogs] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    inventoryApi
      .getLogs()
      .then(setLogs)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false))
  }, [])

  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'productName', label: 'Product' },
    {
      key: 'type',
      label: 'Type',
      render: (r) => (
        <span
          className={`inline-flex rounded-md px-2 py-0.5 text-xs font-semibold ${
            r.type === 'PURCHASE'
              ? 'bg-emerald-500/10 text-emerald-600 dark:text-emerald-400'
              : 'bg-primary-500/10 text-primary-600 dark:text-primary-400'
          }`}
        >
          {r.type}
        </span>
      ),
    },
    { key: 'quantityBefore', label: 'Before' },
    { key: 'quantityAfter', label: 'After' },
    { key: 'reason', label: 'Reason', render: (r) => r.reason || '—' },
    { key: 'createdAt', label: 'Date', render: (r) => formatDate(r.createdAt) },
  ]

  return (
    <div>
      <PageHeader title="Inventory Logs" description="Audit trail of all inventory changes" />

      {error && (
        <div className="mb-4 rounded-xl border border-rose-200 bg-rose-50 p-3 text-sm text-rose-600 dark:border-rose-500/30 dark:bg-rose-500/10 dark:text-rose-400">
          {error}
        </div>
      )}

      {loading ? <LoadingSpinner className="py-12" /> : <DataTable columns={columns} data={logs} />}
    </div>
  )
}
