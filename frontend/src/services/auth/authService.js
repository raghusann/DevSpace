import api from "../../api/axios";
import ENDPOINTS from "../../api/endpoints";

const authService = {
  login: async (data) => {
    const response = await api.post(ENDPOINTS.AUTH.LOGIN, data);
    return response.data;
  },

  register: async (data) => {
    const response = await api.post(ENDPOINTS.AUTH.REGISTER, data);
    return response.data;
  },

  logout: async (refreshToken) => {
    const response = await api.post(ENDPOINTS.AUTH.LOGOUT, {
      refreshToken,
    });

    return response.data;
  },
};

export default authService;