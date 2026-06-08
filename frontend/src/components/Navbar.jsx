import { Link, NavLink } from "react-router-dom";
import { useAuth } from "../context/AuthContext.jsx";

export default function Navbar() {
  const { user, logout } = useAuth();

  return (
    <nav className="sticky top-0 z-20 border-b border-white/60 bg-white/70 backdrop-blur-xl">
      <div className="mx-auto flex max-w-6xl items-center justify-between px-6 py-4">
        <Link to="/" className="font-heading text-xl text-slate">
          NoteBook Portal
        </Link>
        <div className="flex items-center gap-4 text-sm text-slate/80">
          <NavLink className="hover:text-accent" to="/search">
            Search
          </NavLink>
          {user ? (
            <>
              <NavLink className="hover:text-accent flex items-center" to="/dashboard">
                Dashboard
                <span className="ml-2 inline-block h-2 w-2 rounded-full bg-green-500" aria-hidden="true" />
              </NavLink>
              <NavLink className="hover:text-accent" to="/upload">
                Upload
              </NavLink>
              {user.role === "ADMIN" && (
                <NavLink className="hover:text-accent" to="/admin">
                  Admin
                </NavLink>
              )}
              <button className="text-slate/70 hover:text-accent" onClick={logout}>
                Logout
              </button>
            </>
          ) : (
            <>
              <NavLink className="hover:text-accent" to="/login">
                Login
              </NavLink>
              <NavLink className="hover:text-accent" to="/register">
                Register
              </NavLink>
            </>
          )}
        </div>
      </div>
    </nav>
  );
}
