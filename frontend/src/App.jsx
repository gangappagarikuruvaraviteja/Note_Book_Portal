import AppRouter from "./routes/AppRouter.jsx";
import Navbar from "./components/Navbar.jsx";

export default function App() {
  return (
    <div className="min-h-screen page-shell">
      <Navbar />
      <AppRouter />
    </div>
  );
}
