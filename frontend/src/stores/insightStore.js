import { defineStore } from 'pinia';
import { getInsights } from '../api';

export const useInsightStore = defineStore('insight', {
  state: () => ({
    report: '',
    isLoading: false,
    error: null,
  }),
  actions: {
    async fetchInsights(from, to) {
      this.isLoading = true;
      this.error = null;
      this.report = '';
      try {
        const response = await getInsights(from, to);
        this.report = response.data;
      } catch (err) {
        this.error = err.response?.data?.message || 'Failed to fetch insights. Please check backend connection.';
        console.error(err);
        throw err;
      } finally {
        this.isLoading = false;
      }
    },
  },
});
