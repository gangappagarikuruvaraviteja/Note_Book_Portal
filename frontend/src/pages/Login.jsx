import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { loginUser } from "../services/authService.js";
import { useAuth } from "../context/AuthContext.jsx";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const data = await loginUser({ email, password });
      login(data);
      navigate("/dashboard");
    } catch (err) {
      setError(err?.message || "Login failed. Check your credentials.");
    }
  };

  return (
    <section className="mx-auto max-w-lg px-6 py-16">
      <div className="glass-panel animate-fade-up rounded-3xl p-8 shadow-soft">
        <h2 className="font-heading text-2xl text-slate">Welcome back</h2>
        <p className="mt-2 text-sm text-slate/60">Login to manage your notebooks.</p>
        {error && <p className="mt-3 text-sm text-red-600">{error}</p>}
        <form className="mt-6 space-y-4" onSubmit={handleSubmit}>
          <input
            className="w-full rounded-xl border border-orange-100 bg-white px-4 py-3"
            placeholder="Email"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <input
            className="w-full rounded-xl border border-orange-100 bg-white px-4 py-3"
            placeholder="Password"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <button
            className="btn-primary w-full rounded-full bg-accent px-6 py-3 text-sm font-semibold text-white"
            type="submit"
          >
            Sign In
          </button>
        </form>
      </div>
    </section>
  );
}
