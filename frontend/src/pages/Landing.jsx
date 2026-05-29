import { Link } from "react-router-dom";

export default function Landing() {
  return (
    <section className="mx-auto max-w-6xl px-6 py-16 lg:py-20">
      <div className="grid items-center gap-12 lg:grid-cols-[1.1fr_0.9fr]">
        <div className="space-y-6">
          <span className="chip animate-fade-up">AI Powered Notebook Portal</span>
          <h1 className="font-heading text-4xl text-slate lg:text-5xl animate-fade-up delay-1">
            Turn raw notes into smart study assets in minutes.
          </h1>
          <p className="text-base text-slate/70 animate-fade-up delay-2">
            Upload handwritten or PDF notes, auto-tag by subject, and let AI extract
            summaries and exam-ready questions.
          </p>
          <div className="flex flex-wrap gap-4 animate-fade-up delay-3">
            <Link
              className="btn-primary rounded-full bg-accent px-6 py-3 text-sm font-semibold text-white shadow-glow"
              to="/register"
            >
              Get Started
            </Link>
            <Link
              className="rounded-full border border-accent px-6 py-3 text-sm font-semibold text-accent"
              to="/search"
            >
              Explore Notes
            </Link>
          </div>
        </div>
        <div className="glass-panel animate-fade-up delay-2 rounded-3xl p-6 shadow-soft">
          <div className="space-y-4">
            <div className="hover-lift rounded-2xl bg-white p-4 shadow-soft">
              <p className="text-xs uppercase tracking-widest text-slate/60">AI Summary</p>
              <p className="mt-2 text-sm text-slate/80">
                "Operating systems manage hardware resources and provide services for
                programs. Key topics: processes, memory, and scheduling."
              </p>
            </div>
            <div className="hover-lift rounded-2xl bg-white p-4 shadow-soft">
              <p className="text-xs uppercase tracking-widest text-slate/60">Important Questions</p>
              <ul className="mt-2 text-sm text-slate/80 list-disc list-inside">
                <li>Explain round-robin scheduling.</li>
                <li>What is a deadlock? Provide prevention methods.</li>
              </ul>
            </div>
            <div className="hover-lift rounded-2xl bg-white p-4 shadow-soft">
              <p className="text-xs uppercase tracking-widest text-slate/60">Faculty Verified</p>
              <p className="mt-2 text-sm text-slate/80">
                Verified notes get a trust badge and higher discoverability.
              </p>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
}
