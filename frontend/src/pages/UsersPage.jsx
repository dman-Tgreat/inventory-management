import { useEffect, useState } from 'react'
import { Trash2 } from 'lucide-react'
import PageHeader from '../components/PageHeader'
import DataTable from '../components/DataTable'
import LoadingSpinner from '../components/LoadingSpinner'
import { usersApi } from '../api/client'

export default function UsersPage() {
  const [users, setUsers] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  const load = async () => {
    setLoading(true)
    try {
      setUsers(await usersApi.getAll())
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => { load() }, [])

  const handleDelete = async (id) => {
    if (!confirm('Delete this user?')) return
    try {
      await usersApi.delete(id)
      load()
    } catch (err) {
      alert(err.message)
    }
  }

  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'name', label: 'Name' },
    { key: 'email', label: 'Email' },
    {
      key: 'role',
      label: 'Role',
      render: (r) => (
        <span
          className={`inline-flex rounded-md px-2 py-0.5 text-xs font-semibold ${
            r.role?.toUpperCase() === 'ADMIN'
              ? 'bg-primary-500/10 text-primary-600 dark:text-primary-400'
              : 'bg-surface-200 text-surface-700 dark:bg-surface-700 dark:text-surface-200'
          }`}
        >
          {r.role}
        </span>
      ),
    },
    {
      key: 'actions',
      label: 'Actions',
      render: (r) => (
        <button
          onClick={() => handleDelete(r.id)}
          className="rounded-lg p-1.5 text-rose-600 hover:bg-rose-50 dark:text-rose-400 dark:hover:bg-rose-500/10"
        >
          <Trash2 size={15} />
        </button>
      ),
    },
  ]

  return (
    <div>
      <PageHeader title="Users" description="Manage system users and roles" />

      {error && (
        <div className="mb-4 rounded-xl border border-rose-200 bg-rose-50 p-3 text-sm text-rose-600 dark:border-rose-500/30 dark:bg-rose-500/10 dark:text-rose-400">
          {error}
        </div>
      )}

      {loading ? <LoadingSpinner className="py-12" /> : <DataTable columns={columns} data={users} />}
    </div>
  )
}
