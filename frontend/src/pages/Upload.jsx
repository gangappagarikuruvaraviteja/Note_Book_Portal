import { useState } from "react";
import { Link } from "react-router-dom";
import { uploadNotebook } from "../services/notebookService.js";

export default function Upload() {
  const [status, setStatus] = useState("");
  const [form, setForm] = useState({
    title: "",
    subject: "",
    semester: "",
    branch: "",
    description: "",
    tags: "",
    categories: ""
  });
  const [file, setFile] = useState(null);
  const [uploaded, setUploaded] = useState(null);

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (!file) {
      setStatus("Please select a file.");
      return;
    }
    const data = new FormData();
    data.append("file", file);
    data.append("title", form.title);
    try {
      const response = await uploadNotebook(data);
      setStatus("Upload successful.");
      setUploaded(response);
    } catch (err) {
      setStatus(err?.message || "Upload failed.");
    }
  };

  return (
    <section className="mx-auto max-w-3xl px-6 py-12">
      <div className="glass-panel animate-fade-up rounded-3xl p-8 shadow-soft">
        <h2 className="font-heading text-2xl text-slate">Upload notebook</h2>
        <p className="mt-2 text-sm text-slate/60">
          Add metadata so AI can categorize your notes.
        </p>
        {status && <p className="mt-4 text-sm text-slate/70">{status}</p>}
        {uploaded && (
          <div className="mt-4 rounded-2xl bg-white p-4 shadow-soft">
            <p className="text-xs uppercase tracking-widest text-slate/60">Next step</p>
            <p className="mt-2 text-sm text-slate/70">
              View the notebook details and generate the AI summary.
            </p>
            <Link
              className="mt-3 inline-flex items-center text-sm font-semibold text-accent"
              to={`/notebooks/${uploaded.id}`}
            >
              Open notebook →
            </Link>
          </div>
        )}
        <form className="mt-6 space-y-4" onSubmit={handleSubmit}>
          <div>
            <label className="text-xs uppercase tracking-widest text-slate/60">Title</label>
            <input
              className="mt-2 w-full rounded-xl border border-orange-100 bg-white px-4 py-3"
              placeholder="Notebook title"
              value={form.title}
              onChange={(e) => setForm({ ...form, title: e.target.value })}
              required
            />
          </div>
          <div>
            <label className="text-xs uppercase tracking-widest text-slate/60">File</label>
            <input
              className="mt-2 w-full rounded-xl border border-dashed border-orange-200 bg-white px-4 py-3"
              type="file"
              accept=".pdf,image/*"
              onChange={(e) => setFile(e.target.files[0])}
              required
            />
          </div>
          <button
            className="btn-primary rounded-full bg-accent px-6 py-3 text-sm font-semibold text-white"
            type="submit"
          >
            Upload
          </button>
          {uploaded && (
            <Link
              className="inline-flex items-center rounded-full border border-accent px-6 py-3 text-sm font-semibold text-accent"
              to={`/notebooks/${uploaded.id}`}
            >
              Generate summary
            </Link>
          )}
        </form>
      </div>
    </section>
  );
}
