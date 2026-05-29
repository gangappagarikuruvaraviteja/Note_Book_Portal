import { Link } from "react-router-dom";

export default function NotebookCard({ notebook }) {
  return (
    <div className="card-glow hover-lift rounded-2xl p-5 shadow-soft">
      <div className="flex items-start justify-between">
        <div>
          <p className="text-xs uppercase tracking-widest text-slate/60">
            {notebook.subject}
          </p>
          <h3 className="font-heading text-lg text-slate">{notebook.title}</h3>
        </div>
        {notebook.verified && (
          <span className="rounded-full bg-mint/30 px-2 py-1 text-xs font-semibold text-slate">
            Verified
          </span>
        )}
      </div>
      <p className="mt-3 text-sm text-slate/70 line-clamp-3">
        {notebook.description || "No description provided yet."}
      </p>
      <div className="mt-4 flex items-center justify-between text-xs text-slate/60">
        <span>{notebook.semester} • {notebook.branch}</span>
        <span>{notebook.downloadsCount} downloads</span>
      </div>
      <Link
        to={`/notebooks/${notebook.id}`}
        className="mt-4 inline-flex items-center text-sm font-semibold text-accent"
      >
        View details →
      </Link>
    </div>
  );
}
