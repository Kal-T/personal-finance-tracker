import { defineStore } from 'pinia';
import api from '../api';

export const useTransactionStore = defineStore('transaction', {
  state: () => ({
    transactions: [],
    summary: {
      totalIncome: 0,
      totalExpense: 0,
      balance: 0,
    },
    isLoading: false,
    error: null,
  }),
  actions: {
    async fetchAll() {
      this.isLoading = true;
      this.error = null;
      try {
        const response = await api.get('/api/transactions');
        // Sort transactions by date descending, then id descending
        this.transactions = response.data.sort((a, b) => {
          const dateDiff = new Date(b.date) - new Date(a.date);
          if (dateDiff !== 0) return dateDiff;
          return b.id - a.id;
        });
      } catch (err) {
        this.error = err.response?.data?.message || 'Failed to fetch transactions';
        console.error(err);
      } finally {
        this.isLoading = false;
      }
    },

    async fetchSummary() {
      this.isLoading = true;
      this.error = null;
      try {
        const response = await api.get('/api/transactions/summary');
        this.summary = response.data;
      } catch (err) {
        this.error = err.response?.data?.message || 'Failed to fetch summary';
        console.error(err);
      } finally {
        this.isLoading = false;
      }
    },

    async create(data) {
      this.isLoading = true;
      this.error = null;
      try {
        const response = await api.post('/api/transactions', data);
        await this.fetchAll();
        await this.fetchSummary();
        return response.data;
      } catch (err) {
        this.error = err.response?.data?.message || 'Failed to create transaction';
        console.error(err);
        throw err;
      } finally {
        this.isLoading = false;
      }
    },

    async update(id, data) {
      this.isLoading = true;
      this.error = null;
      try {
        const response = await api.put(`/api/transactions/${id}`, data);
        await this.fetchAll();
        await this.fetchSummary();
        return response.data;
      } catch (err) {
        this.error = err.response?.data?.message || 'Failed to update transaction';
        console.error(err);
        throw err;
      } finally {
        this.isLoading = false;
      }
    },

    async remove(id) {
      this.isLoading = true;
      this.error = null;
      try {
        await api.delete(`/api/transactions/${id}`);
        await this.fetchAll();
        await this.fetchSummary();
      } catch (err) {
        this.error = err.response?.data?.message || 'Failed to delete transaction';
        console.error(err);
        throw err;
      } finally {
        this.isLoading = false;
      }
    },
  },
});
