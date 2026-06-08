import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { fetchSummary, getNotebook, recordDownload } from "../services/notebookService.js";

export default function NotebookDetails() {
  const { id } = useParams();
  const [notebook, setNotebook] = useState(null);
  const [summary, setSummary] = useState("");
  const [loadingSummary, setLoadingSummary] = useState(false);
  const [summaryError, setSummaryError] = useState("");

  useEffect(() => {
    const load = async () => {
      const data = await getNotebook(id);
      setNotebook(data);
    };
    load();
  }, [id]);

  const handleSummary = async () => {
    setLoadingSummary(true);
    setSummaryError("");
    try {
      const data = await fetchSummary(id);
      const s = data.summary || "";
      const placeholderKeywords = ["AI disabled", "OPENAI_API_KEY", "GROK_API_KEY", "AI enabled but", "AI request failed"];
      const isPlaceholder = placeholderKeywords.some((k) => s.includes(k));
      if (!s || isPlaceholder) {
        setSummary("");
        setSummaryError(s || "No AI output returned.");
      } else {
        setSummary(s);
      }
    } catch (error) {
      setSummary("");
      setSummaryError(error?.response?.data?.summary || error?.response?.data?.message || error?.message || "Summary generation failed.");
    } finally {
      setLoadingSummary(false);
    }
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
            className="rounded-full border border-accent px-5 py-2 text-sm font-semibold text-accent disabled:cursor-not-allowed disabled:opacity-60"
            onClick={handleSummary}
            disabled={loadingSummary}
          >
            {loadingSummary ? "Generating..." : "Generate summary"}
          </button>
        </div>
        {summaryError && (
          <div className="mt-6 rounded-2xl border border-rose-100 bg-rose-50 p-4 text-sm text-rose-700 animate-fade-up">
            {summaryError}
          </div>
        )}
        <div className={`mt-6 rounded-2xl bg-white p-4 transition-opacity duration-500 ${summary ? 'opacity-100' : 'opacity-0 h-0 overflow-hidden'}`}>
          {summary && (
            <>
              <p className="text-xs uppercase text-slate/60">AI Summary</p>
              <p className="mt-2 text-sm text-slate/70">{summary}</p>
            </>
          )}
        </div>
      </div>
    </section>
  );
}
