import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { fetchSummary, getNotebook, recordDownload } from "../services/notebookService.js";

export default function NotebookDetails() {
  const { id } = useParams();
  const [notebook, setNotebook] = useState(null);
  const [summary, setSummary] = useState("");

  useEffect(() => {
    const load = async () => {
      const data = await getNotebook(id);
      setNotebook(data);
    };
    load();
  }, [id]);

  const handleSummary = async () => {
    const data = await fetchSummary(id);
    setSummary(data.summary);
  };

  const handleDownload = async () => {
    await recordDownload(id);
    window.open(notebook.fileUrl, "_blank");
  };

  if (!notebook) {
    return <div className="px-6 py-12">Loading...</div>;
  }

  return (
    <section className="mx-auto max-w-4xl px-6 py-12">
      <div className="card-glow rounded-3xl p-8 shadow-xl">
        <h2 className="font-heading text-3xl text-slate">{notebook.title}</h2>
        <p className="mt-2 text-sm text-slate/60">
          {notebook.subject} • {notebook.semester} • {notebook.branch}
        </p>
        <p className="mt-4 text-sm text-slate/70">{notebook.description}</p>
        <div className="mt-6 flex flex-wrap gap-3">
          <button
            className="rounded-full bg-accent px-5 py-2 text-sm font-semibold text-white"
            onClick={handleDownload}
          >
            Download
          </button>
          <button
            className="rounded-full border border-accent px-5 py-2 text-sm font-semibold text-accent"
            onClick={handleSummary}
          >
            Generate summary
          </button>
        </div>
        {summary && (
          <div className="mt-6 rounded-2xl bg-white p-4">
            <p className="text-xs uppercase text-slate/60">AI Summary</p>
            <p className="mt-2 text-sm text-slate/70">{summary}</p>
          </div>
        )}
      </div>
    </section>
  );
}
