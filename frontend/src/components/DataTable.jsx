export default function DataTable({ columns, data, emptyMessage = 'No data found' }) {
  if (!data?.length) {
    return (
      <div className="rounded-xl border border-dashed border-surface-200 py-12 text-center text-sm text-surface-700 dark:border-surface-700 dark:text-surface-200">
        {emptyMessage}
      </div>
    )
  }

  return (
    <div className="overflow-x-auto rounded-xl border border-surface-200 dark:border-surface-700">
      <table className="w-full text-left text-sm">
        <thead>
          <tr className="border-b border-surface-200 bg-surface-50 dark:border-surface-700 dark:bg-surface-800/50">
            {columns.map((col) => (
              <th
                key={col.key}
                className="whitespace-nowrap px-4 py-3 text-xs font-semibold uppercase tracking-wider text-surface-700 dark:text-surface-200"
              >
                {col.label}
              </th>
            ))}
          </tr>
        </thead>
        <tbody className="divide-y divide-surface-200 dark:divide-surface-700">
          {data.map((row, i) => (
            <tr
              key={row.id ?? i}
              className="bg-white transition-colors hover:bg-surface-50 dark:bg-surface-900 dark:hover:bg-surface-800/50"
            >
              {columns.map((col) => (
                <td key={col.key} className="whitespace-nowrap px-4 py-3 text-surface-900 dark:text-surface-100">
                  {col.render ? col.render(row) : row[col.key]}
                </td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
