import axios from "axios";
import useAuthStore from "../store/authStore";
import ENDPOINTS from "./endpoints";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "http://localhost:8080",
  headers: { "Content-Type": "application/json" },
});

api.interceptors.request.use((config) => {
  const token = useAuthStore.getState().accessToken;
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const original = error.config;
    if (error.response?.status === 401 && !original._retry) {
      original._retry = true;
      const refreshToken = useAuthStore.getState().refreshToken;
      if (refreshToken) {
        try {
          const { data } = await axios.post(
            `${api.defaults.baseURL}${ENDPOINTS.AUTH.REFRESH}`,
            { refreshToken }
          );
          const auth = data.data;
          useAuthStore.getState().setAuth({
            user: auth.user,
            accessToken: auth.accessToken,
            refreshToken: auth.refreshToken,
          });
          original.headers.Authorization = `Bearer ${auth.accessToken}`;
          return api(original);
        } catch {
          useAuthStore.getState().logout();
          window.location.href = "/login";
        }
      }
    }
    return Promise.reject(error);
  }
);

export default api;
