import { Link, NavLink } from "react-router-dom";
import Navbar from "./Navbar.jsx";
import Chatbot from "./Chatbot.jsx";
import { useState } from "react";

export default function AppShell({ children }) {
  const [chatOpen, setChatOpen] = useState(false);

  return (
    <div className="min-h-screen bg-slate/5">
      <Navbar />
      <div className="flex max-w-7xl mx-auto">
        <aside className="w-64 border-r bg-white/90 min-h-[calc(100vh-64px)]">
          <div className="p-4">
            <Link to="/" className="text-lg font-heading">Notebook Portal</Link>
            <div className="mt-6 space-y-2">
              <NavLink to="/dashboard" className={({ isActive }) => isActive ? 'block px-3 py-2 rounded bg-slate/100 font-semibold' : 'block px-3 py-2 rounded hover:bg-slate/50'}>All Notes</NavLink>
              <NavLink to="/search" className={({ isActive }) => isActive ? 'block px-3 py-2 rounded bg-slate/100 font-semibold' : 'block px-3 py-2 rounded hover:bg-slate/50'}>Search</NavLink>
              <NavLink to="/upload" className={({ isActive }) => isActive ? 'block px-3 py-2 rounded bg-slate/100 font-semibold' : 'block px-3 py-2 rounded hover:bg-slate/50'}>Upload</NavLink>
            </div>
          </div>
        </aside>

        <main className="flex-1 p-8">
          {children}
        </main>

        <div className="w-12 flex items-end justify-center p-2">
          <button onClick={() => setChatOpen(true)} className="bg-accent text-white px-3 py-2 rounded-md">Chat</button>
        </div>
      </div>

      <Chatbot open={chatOpen} onClose={() => setChatOpen(false)} />
    </div>
  );
}
