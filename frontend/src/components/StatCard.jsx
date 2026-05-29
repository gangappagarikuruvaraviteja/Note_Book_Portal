export default function StatCard({ label, value }) {
  return (
    <div className="rounded-2xl bg-white/90 p-5 shadow-lg">
      <p className="text-xs uppercase tracking-widest text-slate/60">{label}</p>
      <p className="mt-2 text-2xl font-heading text-slate">{value}</p>
    </div>
  );
}
