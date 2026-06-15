import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const getInsights = (from, to) => {
  return api.get('/api/ai/analyze', {
    params: { from, to },
  });
};

export default api;

