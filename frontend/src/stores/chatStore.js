import { defineStore } from 'pinia';

export const useChatStore = defineStore('chat', {
  state: () => ({
    messages: [],
    loading: false,
    error: null,
  }),
  actions: {
    // Empty placeholders for now
  },
});
