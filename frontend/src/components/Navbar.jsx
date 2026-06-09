import { Link, NavLink } from "react-router-dom";
import { useState } from "react";
import { useAuth } from "../context/AuthContext.jsx";

export default function Navbar() {
  const { user, logout } = useAuth();
  const [open, setOpen] = useState(false);

  return (
    <nav className="sticky top-0 z-20 border-b border-white/60 bg-white/70 backdrop-blur-xl">
      <div className="mx-auto flex max-w-6xl items-center justify-between px-6 py-4">
        <Link to="/" className="font-heading text-xl text-slate">
          NoteBook Portal
        </Link>

        <button
          className="md:hidden text-slate/80"
          aria-label="Toggle menu"
          onClick={() => setOpen((v) => !v)}
        >
          <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M4 6H20M4 12H20M4 18H20" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
          </svg>
        </button>

        <div className="hidden md:flex items-center gap-4 text-sm text-slate/80">
          <NavLink to="/search" className={({ isActive }) => isActive ? 'text-accent font-semibold' : 'hover:text-accent'}>
            Search
          </NavLink>
          {user ? (
            <>
              <NavLink to="/dashboard" className={({ isActive }) => isActive ? 'text-accent font-semibold' : 'hover:text-accent'}>
                Dashboard
              </NavLink>
              <NavLink to="/upload" className={({ isActive }) => isActive ? 'text-accent font-semibold' : 'hover:text-accent'}>
                Upload
              </NavLink>
              {user.role === "ADMIN" && (
                <NavLink to="/admin" className={({ isActive }) => isActive ? 'text-accent font-semibold' : 'hover:text-accent'}>
                  Admin
                </NavLink>
              )}
              <button className="text-slate/70 hover:text-accent" onClick={logout}>
                Logout
              </button>
            </>
          ) : (
            <>
              <NavLink to="/login" className={({ isActive }) => isActive ? 'text-accent font-semibold' : 'hover:text-accent'}>
                Login
              </NavLink>
              <NavLink to="/register" className={({ isActive }) => isActive ? 'text-accent font-semibold' : 'hover:text-accent'}>
                Register
              </NavLink>
            </>
          )}
        </div>
      </div>

      {open && (
        <div className="md:hidden border-t border-white/60 bg-white/60">
          <div className="flex flex-col gap-2 px-4 py-3 text-sm text-slate/80">
            <NavLink to="/search" onClick={() => setOpen(false)} className={({ isActive }) => isActive ? 'text-accent font-semibold' : 'hover:text-accent'}>
              Search
            </NavLink>
            {user ? (
              <>
                <NavLink to="/dashboard" onClick={() => setOpen(false)} className={({ isActive }) => isActive ? 'text-accent font-semibold' : 'hover:text-accent'}>
                  Dashboard
                </NavLink>
                <NavLink to="/upload" onClick={() => setOpen(false)} className={({ isActive }) => isActive ? 'text-accent font-semibold' : 'hover:text-accent'}>
                  Upload
                </NavLink>
                {user.role === "ADMIN" && (
                  <NavLink to="/admin" onClick={() => setOpen(false)} className={({ isActive }) => isActive ? 'text-accent font-semibold' : 'hover:text-accent'}>
                    Admin
                  </NavLink>
                )}
                <button className="text-left text-slate/70 hover:text-accent" onClick={() => { setOpen(false); logout(); }}>
                  Logout
                </button>
              </>
            ) : (
              <>
                <NavLink to="/login" onClick={() => setOpen(false)} className={({ isActive }) => isActive ? 'text-accent font-semibold' : 'hover:text-accent'}>
                  Login
                </NavLink>
                <NavLink to="/register" onClick={() => setOpen(false)} className={({ isActive }) => isActive ? 'text-accent font-semibold' : 'hover:text-accent'}>
                  Register
                </NavLink>
              </>
            )}
          </div>
        </div>
      )}
    </nav>
  );
}
