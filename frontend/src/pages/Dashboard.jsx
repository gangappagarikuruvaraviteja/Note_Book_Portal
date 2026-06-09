export default function Dashboard() {
  return (
    <section className="flex min-h-[72vh] flex-col">
      <div className="mb-10 pt-4">
        <h1 className="text-[52px] font-bold tracking-tight text-slate-950">All Notes</h1>
        <p className="mt-2 text-[18px] text-slate-500">0 notes · sorted by last edited</p>
      </div>

      <div className="flex flex-1 items-center justify-center">
        <div className="flex flex-col items-center text-center">
          <div className="flex h-20 w-20 items-center justify-center rounded-2xl border-2 border-slate-200 bg-slate-50 text-slate-300 shadow-sm">
            <svg className="h-10 w-10" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round" d="M7 3.5h6l4 4V20.5A1.5 1.5 0 0115.5 22h-8A1.5 1.5 0 016 20.5v-15A1.5 1.5 0 017.5 3h-.5z" />
              <path strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round" d="M13 3.5v4h4" />
              <path strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round" d="M9 12h6M9 15h6" />
            </svg>
          </div>
          <p className="mt-8 text-[20px] text-slate-500">Click + New Note in the sidebar to get started.</p>
        </div>
      </div>
    </section>
  );
}
