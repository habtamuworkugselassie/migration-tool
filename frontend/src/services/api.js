import axios from 'axios';

const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json'
  }
});

export const clientService = {
  async getLegacyClients() {
    const response = await api.get('/legacy-clients');
    return response.data;
  },

  async getNewClients() {
    const response = await api.get('/new-clients');
    return response.data;
  },

  async migrateClient(id) {
    const response = await api.post(`/migrate/${id}`);
    return response.data;
  }
};

export default api;

