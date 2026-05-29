import { useEffect, useState } from "react";
import api from "../services/api.js";
import StatCard from "../components/StatCard.jsx";

export default function AdminPanel() {
  const [stats, setStats] = useState(null);

  useEffect(() => {
    const load = async () => {
      const response = await api.get("/api/admin/stats");
      setStats(response.data);
    };
    load();
  }, []);

  if (!stats) {
    return <div className="px-6 py-12">Loading...</div>;
  }

  return (
    <section className="mx-auto max-w-6xl px-6 py-12">
      <h2 className="font-heading text-3xl text-slate">Admin analytics</h2>
      <div className="mt-6 grid gap-6 md:grid-cols-3">
        <StatCard label="Total users" value={stats.totalUsers} />
        <StatCard label="Total uploads" value={stats.totalUploads} />
        <StatCard label="Total downloads" value={stats.totalDownloads} />
      </div>
      <div className="mt-8 rounded-3xl bg-white/80 p-6 shadow-xl">
        <h3 className="font-heading text-xl text-slate">Most downloaded</h3>
        <p className="mt-2 text-sm text-slate/70">
          {stats.mostDownloadedTitle || "No data yet"} • {stats.mostDownloadedCount}
        </p>
      </div>
    </section>
  );
}
