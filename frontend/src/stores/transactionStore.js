import { defineStore } from 'pinia';

export const useTransactionStore = defineStore('transaction', {
  state: () => ({
    transactions: [],
    loading: false,
    error: null,
  }),
  actions: {
    // Empty placeholders for now
  },
});
