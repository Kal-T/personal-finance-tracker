<template>
  <div class="space-y-6">
    <h1 class="text-3xl font-extrabold text-gray-900 tracking-tight">AI Financial Assistant</h1>
    <p class="text-gray-600">Ask questions about your transactions, budgets, or get financial insights.</p>
    <div class="max-w-4xl mx-auto">
      <ChatPanel />
    </div>
  </div>
</template>

<script>
import ChatPanel from '../components/ChatPanel.vue';

export default {
  name: 'ChatView',
  components: {
    ChatPanel
  }
}
</script>
