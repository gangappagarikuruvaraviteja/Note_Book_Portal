import api from "./api";

const getApiErrorMessage = (error, fallback) => {
  if (error?.response?.data?.message) {
    return error.response.data.message;
  }
  if (error?.message) {
    return error.message;
  }
  return fallback;
};

export const registerUser = async (payload) => {
  try {
    const response = await api.post("/api/auth/register", payload);
    return response.data;
  } catch (error) {
    throw new Error(getApiErrorMessage(error, "Registration failed."));
  }
};

export const loginUser = async (payload) => {
  try {
    const response = await api.post("/api/auth/login", payload);
    return response.data;
  } catch (error) {
    throw new Error(getApiErrorMessage(error, "Login failed."));
  }
};
