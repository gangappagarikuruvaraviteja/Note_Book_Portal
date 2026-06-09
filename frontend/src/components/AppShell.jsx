import { Link, NavLink } from "react-router-dom";
import Navbar from "./Navbar.jsx";
import Chatbot from "./Chatbot.jsx";
import { useState } from "react";

export default function AppShell({ children }) {
  const [chatOpen, setChatOpen] = useState(false);

  return (
    <div className="min-h-screen bg-gradient-to-r from-slate-50 to-slate-100">
      <Navbar />
      <div className="flex max-w-7xl mx-auto">
        <aside className="w-72 bg-white shadow-sm min-h-[calc(100vh-64px)] border-r">
          <div className="px-5 py-6">
            <div className="flex items-center justify-between">
              <div>
                <div className="w-10 h-10 bg-slate-900 rounded-md flex items-center justify-center text-white font-bold">NB</div>
                <div className="text-xs text-slate/60">Portal</div>
              </div>
              <div></div>
            </div>

            <button className="mt-4 w-full bg-slate-900 text-white rounded-md px-4 py-2 flex items-center gap-3">
              <span className="text-lg">+</span>
              <span>New Note</span>
            </button>

            <nav className="mt-6">
              <NavLink to="/dashboard" className={({ isActive }) => isActive ? 'flex items-center gap-3 px-3 py-2 rounded bg-slate-100 font-semibold' : 'flex items-center gap-3 px-3 py-2 rounded hover:bg-slate/50 text-slate/90'}>
                <svg className="w-5 h-5 text-slate/70" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" d="M3 7h18M3 12h18M3 17h18"/></svg>
                All Notes
              </NavLink>
              <NavLink to="/search" className={({ isActive }) => isActive ? 'flex items-center gap-3 px-3 py-2 rounded bg-slate-100 font-semibold' : 'flex items-center gap-3 px-3 py-2 rounded hover:bg-slate/50 text-slate/90'}>
                <svg className="w-5 h-5 text-slate/70" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" d="M21 21l-4.35-4.35"/></svg>
                Search
              </NavLink>
              <NavLink to="/upload" className={({ isActive }) => isActive ? 'flex items-center gap-3 px-3 py-2 rounded bg-slate-100 font-semibold' : 'flex items-center gap-3 px-3 py-2 rounded hover:bg-slate/50 text-slate/90'}>
                <svg className="w-5 h-5 text-slate/70" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" d="M12 4v16"/></svg>
                Upload
              </NavLink>
            </nav>

            <div className="mt-6 text-slate/60 text-sm">Notebooks</div>
            <div className="mt-2">
              <button className="text-sm text-accent">+ New notebook</button>
            </div>
          </div>

          <div className="absolute bottom-6 left-0 right-0 px-5">
            <div className="flex items-center gap-3 text-slate/80 py-2">
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" d="M3 6h18M10 6v12"/></svg>
              <span>Trash</span>
            </div>
            <div className="flex items-center gap-3 text-slate/80 py-2">
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" d="M12 6v6l4 2"/></svg>
              <span>Settings</span>
            </div>
          </div>
        </aside>

        <main className="flex-1 p-8">
          <div className="rounded-3xl bg-white p-8 shadow-lg min-h-[70vh]">
            {children}
          </div>
        </main>

        <div className="w-16 flex items-end justify-center p-4">
          <button onClick={() => setChatOpen(true)} className="bg-accent text-white px-3 py-2 rounded-md">Chat</button>
        </div>
      </div>

      <Chatbot open={chatOpen} onClose={() => setChatOpen(false)} />
    </div>
  );
}
