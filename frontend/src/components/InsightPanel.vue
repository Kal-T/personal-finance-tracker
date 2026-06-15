<template>
  <div class="bg-white shadow rounded-lg border border-gray-100 overflow-hidden">
    <!-- Header -->
    <div class="px-6 py-5 border-b border-gray-200 bg-gray-50 flex items-center justify-between">
      <div class="flex items-center space-x-2">
        <!-- Sparkle AI Icon -->
        <svg class="h-6 w-6 text-primary-600 animate-pulse" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
        </svg>
        <h3 class="text-lg font-bold text-gray-900">AI Financial Insights</h3>
      </div>
      <span class="text-xs font-semibold text-primary-600 bg-primary-50 px-2 py-1 rounded-full uppercase tracking-wider">
        Gemini 1.5 Flash
      </span>
    </div>

    <!-- Date selector & trigger -->
    <div class="p-6 border-b border-gray-100 bg-white">
      <div class="flex flex-col sm:flex-row sm:items-end sm:space-x-4 space-y-4 sm:space-y-0">
        <div class="flex-1">
          <label for="from-date" class="block text-xs font-semibold text-gray-500 uppercase tracking-wider">From Date</label>
          <input
            id="from-date"
            v-model="dates.from"
            type="date"
            class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-900 focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
          />
        </div>
        <div class="flex-1">
          <label for="to-date" class="block text-xs font-semibold text-gray-500 uppercase tracking-wider">To Date</label>
          <input
            id="to-date"
            v-model="dates.to"
            type="date"
            class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm text-gray-900 focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
          />
        </div>
        <div>
          <button
            @click="generateReport"
            :disabled="isLoading"
            class="w-full sm:w-auto inline-flex items-center justify-center px-5 py-2.5 border border-transparent text-sm font-semibold rounded-md shadow-sm text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 transition-colors disabled:opacity-50"
          >
            <!-- Loading indicator -->
            <svg
              v-if="isLoading"
              class="animate-spin -ml-1 mr-3 h-5 w-5 text-white"
              fill="none"
              viewBox="0 0 24 24"
            >
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" />
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z" />
            </svg>
            Generate Insights
          </button>
        </div>
      </div>
    </div>

    <!-- Insight Content Container -->
    <div class="p-6 bg-white min-h-[150px] relative">
      <!-- Loading state -->
      <div v-if="isLoading" class="absolute inset-0 bg-white bg-opacity-75 flex flex-col items-center justify-center space-y-3">
        <div class="text-primary-600 font-semibold text-sm">Analyzing transaction history...</div>
        <p class="text-xs text-gray-400">Gemini is looking for patterns and saving opportunities.</p>
      </div>

      <!-- Error message -->
      <div v-if="error" class="p-4 bg-red-50 border border-red-200 rounded-md flex items-start space-x-3 text-red-700">
        <svg class="h-5 w-5 text-red-500 flex-shrink-0 mt-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
        </svg>
        <div class="text-sm font-medium">
          {{ error }}
        </div>
      </div>

      <!-- Report Display -->
      <div v-else-if="report" class="prose max-w-none">
        <div class="whitespace-pre-line leading-relaxed text-gray-700 tracking-wide text-sm sm:text-base bg-primary-50/30 p-5 rounded-lg border border-primary-100/50">
          {{ report }}
        </div>
      </div>

      <!-- Empty/Init state -->
      <div v-else class="flex flex-col items-center justify-center text-center py-10 space-y-2">
        <svg class="h-12 w-12 text-gray-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
        </svg>
        <h4 class="text-sm font-bold text-gray-600">No Insights Generated Yet</h4>
        <p class="text-xs text-gray-400 max-w-xs">Select a date range and click "Generate Insights" to receive custom advice from Gemini.</p>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from 'pinia';
import { useInsightStore } from '../stores/insightStore';

export default {
  name: 'InsightPanel',
  data() {
    // Default range: First day of current month to today
    const now = new Date();
    const firstDay = new Date(now.getFullYear(), now.getMonth(), 1).toISOString().substring(0, 10);
    const today = now.toISOString().substring(0, 10);

    return {
      dates: {
        from: firstDay,
        to: today,
      },
    };
  },
  computed: {
    ...mapState(useInsightStore, ['report', 'isLoading', 'error']),
  },
  methods: {
    ...mapActions(useInsightStore, ['fetchInsights']),
    async generateReport() {
      try {
        await this.fetchInsights(this.dates.from, this.dates.to);
      } catch (err) {
        // Error state handled in store
      }
    },
  },
};
</script>
