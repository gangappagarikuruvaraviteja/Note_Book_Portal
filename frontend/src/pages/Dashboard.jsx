import { useAuth } from "../context/AuthContext.jsx";
import StatCard from "../components/StatCard.jsx";

export default function Dashboard() {
  const { user } = useAuth();

  return (
    <section className="mx-auto max-w-6xl px-6 py-12">
      <div className="flex items-center justify-between">
        <div>
          <p className="text-xs uppercase tracking-[0.3em] text-slate/60">Dashboard</p>
          <h2 className="font-heading text-3xl text-slate">Welcome, {user?.name}</h2>
        </div>
      </div>
      <div className="mt-8 grid gap-6 md:grid-cols-3">
        <StatCard label="Uploads" value="0" />
        <StatCard label="Downloads" value="0" />
        <StatCard label="Average rating" value="0.0" />
      </div>
      <div className="mt-8 rounded-3xl bg-white/80 p-6 shadow-xl">
        <h3 className="font-heading text-xl text-slate">Next steps</h3>
        <p className="mt-2 text-sm text-slate/70">
          No dashboard activity yet. Upload your first notebook to start tracking
          stats, AI summaries, and downloads.
        </p>
      </div>
    </section>
  );
}
