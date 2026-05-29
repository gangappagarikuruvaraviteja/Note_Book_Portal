import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { registerUser } from "../services/authService.js";
import { useAuth } from "../context/AuthContext.jsx";

export default function Register() {
  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    role: "STUDENT"
  });
  const [error, setError] = useState("");
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const data = await registerUser(form);
      login(data);
      navigate("/dashboard");
    } catch (err) {
      setError(err?.message || "Registration failed. Try another email.");
    }
  };

  return (
    <section className="mx-auto max-w-lg px-6 py-16">
      <div className="glass-panel animate-fade-up rounded-3xl p-8 shadow-soft">
        <h2 className="font-heading text-2xl text-slate">Create account</h2>
        <p className="mt-2 text-sm text-slate/60">Start uploading smarter notes.</p>
        {error && <p className="mt-3 text-sm text-red-600">{error}</p>}
        <form className="mt-6 space-y-4" onSubmit={handleSubmit}>
          <input
            className="w-full rounded-xl border border-orange-100 bg-white px-4 py-3"
            placeholder="Full name"
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
            required
          />
          <input
            className="w-full rounded-xl border border-orange-100 bg-white px-4 py-3"
            placeholder="Email"
            type="email"
            value={form.email}
            onChange={(e) => setForm({ ...form, email: e.target.value })}
            required
          />
          <input
            className="w-full rounded-xl border border-orange-100 bg-white px-4 py-3"
            placeholder="Password"
            type="password"
            value={form.password}
            onChange={(e) => setForm({ ...form, password: e.target.value })}
            required
          />
          <select
            className="w-full rounded-xl border border-orange-100 bg-white px-4 py-3"
            value={form.role}
            onChange={(e) => setForm({ ...form, role: e.target.value })}
          >
            <option value="STUDENT">Student</option>
            <option value="FACULTY">Faculty</option>
            <option value="ADMIN">Admin</option>
          </select>
          <button
            className="btn-primary w-full rounded-full bg-accent px-6 py-3 text-sm font-semibold text-white"
            type="submit"
          >
            Create Account
          </button>
        </form>
      </div>
    </section>
  );
}
