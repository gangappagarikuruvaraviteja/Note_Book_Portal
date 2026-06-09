import { Route, Routes } from "react-router-dom";
import Landing from "../pages/Landing.jsx";
import Login from "../pages/Login.jsx";
import Register from "../pages/Register.jsx";
import Dashboard from "../pages/Dashboard.jsx";
import Upload from "../pages/Upload.jsx";
import Search from "../pages/Search.jsx";
import NotebookDetails from "../pages/NotebookDetails.jsx";
import AdminPanel from "../pages/AdminPanel.jsx";
import AppShell from "../components/AppShell.jsx";
import ProtectedRoute from "../components/ProtectedRoute.jsx";

export default function AppRouter() {
  return (
    <Routes>
      <Route path="/" element={<Landing />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route
        path="/dashboard"
        element={
          <ProtectedRoute>
            <AppShell>
              <Dashboard />
            </AppShell>
          </ProtectedRoute>
        }
      />
      <Route
        path="/upload"
        element={
          <ProtectedRoute>
            <AppShell>
              <Upload />
            </AppShell>
          </ProtectedRoute>
        }
      />
      <Route path="/search" element={<AppShell><Search /></AppShell>} />
      <Route path="/notebooks/:id" element={<AppShell><NotebookDetails /></AppShell>} />
      <Route
        path="/admin"
        element={
          <ProtectedRoute requiredRole="ADMIN">
            <AppShell>
              <AdminPanel />
            </AppShell>
          </ProtectedRoute>
        }
      />
    </Routes>
  );
}
