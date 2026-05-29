import { Route, Routes } from "react-router-dom";
import Landing from "../pages/Landing.jsx";
import Login from "../pages/Login.jsx";
import Register from "../pages/Register.jsx";
import Dashboard from "../pages/Dashboard.jsx";
import Upload from "../pages/Upload.jsx";
import Search from "../pages/Search.jsx";
import NotebookDetails from "../pages/NotebookDetails.jsx";
import AdminPanel from "../pages/AdminPanel.jsx";
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
            <Dashboard />
          </ProtectedRoute>
        }
      />
      <Route
        path="/upload"
        element={
          <ProtectedRoute>
            <Upload />
          </ProtectedRoute>
        }
      />
      <Route path="/search" element={<Search />} />
      <Route path="/notebooks/:id" element={<NotebookDetails />} />
      <Route
        path="/admin"
        element={
          <ProtectedRoute requiredRole="ADMIN">
            <AdminPanel />
          </ProtectedRoute>
        }
      />
    </Routes>
  );
}
