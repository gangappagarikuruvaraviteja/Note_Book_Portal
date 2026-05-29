import { createContext, useContext, useMemo, useState } from "react";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const stored = localStorage.getItem("np_user");
    return stored ? JSON.parse(stored) : null;
  });

  const login = (data) => {
    const payload = {
      id: data.userId,
      name: data.name,
      role: data.role,
      token: data.token
    };
    localStorage.setItem("np_user", JSON.stringify(payload));
    setUser(payload);
  };

  const logout = () => {
    localStorage.removeItem("np_user");
    setUser(null);
  };

  const value = useMemo(() => ({ user, login, logout }), [user]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  return useContext(AuthContext);
}
