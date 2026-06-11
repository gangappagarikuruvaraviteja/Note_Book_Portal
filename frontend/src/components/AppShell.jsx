import { Link, NavLink } from "react-router-dom";
import Chatbot from "./Chatbot.jsx";
import { useState } from "react";

export default function AppShell({ children }) {
  const [chatOpen, setChatOpen] = useState(false);

  return (
    <div className="min-h-screen w-full overflow-hidden bg-slate-50">
      <div className="flex min-h-screen w-full">
        <aside className="relative flex w-[280px] flex-col border-r border-slate-200 bg-[#f8fafc]">
          <div className="border-b border-slate-200 px-3 py-3">
            <div className="flex items-start gap-3">
              <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-gradient-to-br from-accent to-orange-600 text-white shadow-md">
                <svg className="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round" d="M4 5.5A1.5 1.5 0 015.5 4h4A1.5 1.5 0 0111 5.5v13A1.5 1.5 0 019.5 20h-4A1.5 1.5 0 014 18.5v-13zM13 5.5A1.5 1.5 0 0114.5 4h4A1.5 1.5 0 0120 5.5v13a1.5 1.5 0 01-1.5 1.5h-4a1.5 1.5 0 01-1.5-1.5v-13z" />
                </svg>
              </div>
              <div className="leading-tight">
                <div className="text-[22px] font-bold text-slate-900">Notebook</div>
                <div className="text-sm font-medium text-accent">Portal</div>
              </div>
            </div>

            <button className="mt-3 flex w-full items-center gap-3 rounded-xl bg-gradient-to-r from-accent to-orange-600 px-4 py-3 text-left font-semibold text-white shadow-md transition hover:shadow-lg hover:scale-105">
              <span className="text-xl leading-none font-bold">+</span>
              <span>New Note</span>
            </button>
          </div>

          <nav className="px-2 py-2 text-[15px] text-slate-900">
            <NavLink to="/dashboard" className={({ isActive }) => isActive ? 'mb-1 flex items-center gap-3 rounded-xl bg-slate-100 px-3 py-3 font-semibold shadow-sm ring-1 ring-slate-200' : 'mb-1 flex items-center gap-3 rounded-xl px-3 py-3 hover:bg-slate-100'}>
              <svg className="h-5 w-5 text-slate-700" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeWidth="1.7" strokeLinecap="round" strokeLinejoin="round" d="M4 5.5h16M4 12h16M4 18.5h16" /></svg>
              All Notes
            </NavLink>
            <NavLink to="/search" className={({ isActive }) => isActive ? 'mb-1 flex items-center gap-3 rounded-xl bg-slate-100 px-3 py-3 font-semibold shadow-sm ring-1 ring-slate-200' : 'mb-1 flex items-center gap-3 rounded-xl px-3 py-3 hover:bg-slate-100'}>
              <svg className="h-5 w-5 text-slate-700" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeWidth="1.7" strokeLinecap="round" strokeLinejoin="round" d="M21 21l-4.25-4.25" /><circle cx="11" cy="11" r="6.5" strokeWidth="1.7" /></svg>
              Search
            </NavLink>
            <NavLink to="/upload" className={({ isActive }) => isActive ? 'mb-1 flex items-center gap-3 rounded-xl bg-slate-100 px-3 py-3 font-semibold shadow-sm ring-1 ring-slate-200' : 'mb-1 flex items-center gap-3 rounded-xl px-3 py-3 hover:bg-slate-100'}>
              <svg className="h-5 w-5 text-slate-700" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeWidth="1.7" strokeLinecap="round" strokeLinejoin="round" d="M12 5v14M5 12h14" /></svg>
              Upload
            </NavLink>
          </nav>

          <div className="px-5 pt-6">
            <div className="text-[14px] font-semibold text-slate-700">Notebooks</div>
            <button className="mt-2 flex items-center gap-3 text-[15px] text-slate-500 hover:text-slate-900">
              <span className="text-2xl leading-none text-slate-400">+</span>
              <span>New notebook</span>
            </button>
          </div>

          <div className="mt-auto border-t border-slate-200 px-3 py-4">
            <button className="flex w-full items-center gap-3 rounded-xl px-3 py-3 text-left text-slate-700 hover:bg-slate-100">
              <svg className="h-5 w-5 text-slate-700" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeWidth="1.7" strokeLinecap="round" strokeLinejoin="round" d="M4 7h16M8 7V5a2 2 0 012-2h4a2 2 0 012 2v2M6 7l1 13h10l1-13" /></svg>
              Trash
            </button>
            <button className="mt-1 flex w-full items-center gap-3 rounded-xl px-3 py-3 text-left text-slate-700 hover:bg-slate-100">
              <svg className="h-5 w-5 text-slate-700" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeWidth="1.7" strokeLinecap="round" strokeLinejoin="round" d="M12 6v6l4 2" /><circle cx="12" cy="12" r="9" strokeWidth="1.7" /></svg>
              Settings
            </button>
          </div>
        </aside>

        <main className="flex-1 bg-white">
          <div className="flex h-full min-h-screen flex-col">
            <div className="flex h-12 items-center border-b border-slate-200 px-4">
              <button className="rounded-md p-1 text-slate-700 hover:bg-slate-100" aria-label="Toggle sidebar">
                <svg className="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeWidth="1.7" strokeLinecap="round" strokeLinejoin="round" d="M4 5.5h16M4 12h16M4 18.5h16" /></svg>
              </button>
            </div>

            <div className="flex-1 px-10 py-12">
              <div className="w-full">
                {children}
              </div>
            </div>
          </div>
        </main>

        <div className="hidden w-16 items-end justify-center p-4 xl:flex">
          <button onClick={() => setChatOpen(true)} className="rounded-md bg-accent px-3 py-2 text-white shadow-sm">Chat</button>
        </div>
      </div>

      <Chatbot open={chatOpen} onClose={() => setChatOpen(false)} />
    </div>
  );
}
