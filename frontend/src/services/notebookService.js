import api from "./api";

const getApiErrorMessage = (error, fallback) => {
  const status = error?.response?.status;
  if (status === 401 || status === 403) {
    return "Session expired. Please log in again.";
  }
  if (error?.response?.data?.message) {
    return error.response.data.message;
  }
  if (error?.message) {
    return error.message;
  }
  return fallback;
};

export const searchNotebooks = async (params) => {
  const response = await api.get("/api/notebooks/search", { params });
  return response.data;
};

export const getNotebook = async (id) => {
  const response = await api.get(`/api/notebooks/${id}`);
  return response.data;
};

export const uploadNotebook = async (formData) => {
  try {
    const response = await api.post("/api/notebooks/upload", formData, {
      headers: { "Content-Type": "multipart/form-data" }
    });
    return response.data;
  } catch (error) {
    throw new Error(getApiErrorMessage(error, "Upload failed."));
  }
};

export const recordDownload = async (id) => {
  const response = await api.post(`/api/notebooks/${id}/download`);
  return response.data;
};

export const fetchSummary = async (id) => {
  const response = await api.get(`/api/notebooks/${id}/ai/summary`);
  return response.data;
};
