<template>
  <div class="space-y-8">
    <!-- Header -->
    <div class="border-b border-gray-200 pb-5">
      <h1 class="text-3xl font-extrabold text-gray-900 tracking-tight">Financial Dashboard</h1>
      <p class="mt-2 text-sm text-gray-500">Overview of your current month's transactions and AI-driven insights.</p>
    </div>

    <!-- Summary Cards (Current Month) -->
    <div class="space-y-4">
      <h2 class="text-lg font-bold text-gray-700">Current Month Activity ({{ currentMonthName }})</h2>
      <div class="grid grid-cols-1 gap-5 sm:grid-cols-3">
        <!-- Income -->
        <div class="bg-white overflow-hidden shadow rounded-lg border border-gray-100">
          <div class="p-5">
            <div class="flex items-center">
              <div class="flex-shrink-0 bg-green-50 rounded-md p-3">
                <!-- green up arrow icon -->
                <svg class="h-6 w-6 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 10l7-7m0 0l7 7m-7-7v18" />
                </svg>
              </div>
              <div class="ml-5 w-0 flex-1">
                <dl>
                  <dt class="text-sm font-medium text-gray-500 truncate">Month Income</dt>
                  <dd class="text-2xl font-bold text-green-600">${{ formatCurrency(currentMonthSummary.totalIncome) }}</dd>
                </dl>
              </div>
            </div>
          </div>
        </div>

        <!-- Expense -->
        <div class="bg-white overflow-hidden shadow rounded-lg border border-gray-100">
          <div class="p-5">
            <div class="flex items-center">
              <div class="flex-shrink-0 bg-red-50 rounded-md p-3">
                <!-- red down arrow icon -->
                <svg class="h-6 w-6 text-red-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 14l-7 7m0 0l-7-7m7 7V3" />
                </svg>
              </div>
              <div class="ml-5 w-0 flex-1">
                <dl>
                  <dt class="text-sm font-medium text-gray-500 truncate">Month Expense</dt>
                  <dd class="text-2xl font-bold text-red-600">${{ formatCurrency(currentMonthSummary.totalExpense) }}</dd>
                </dl>
              </div>
            </div>
          </div>
        </div>

        <!-- Balance -->
        <div class="bg-white overflow-hidden shadow rounded-lg border border-gray-100">
          <div class="p-5">
            <div class="flex items-center">
              <div
                class="flex-shrink-0 rounded-md p-3"
                :class="[currentMonthSummary.balance >= 0 ? 'bg-primary-50' : 'bg-amber-50']"
              >
                <!-- Balance icon -->
                <svg
                  class="h-6 w-6"
                  :class="[currentMonthSummary.balance >= 0 ? 'text-primary-600' : 'text-amber-600']"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              </div>
              <div class="ml-5 w-0 flex-1">
                <dl>
                  <dt class="text-sm font-medium text-gray-500 truncate">Month Balance</dt>
                  <dd
                    class="text-2xl font-bold"
                    :class="[currentMonthSummary.balance >= 0 ? 'text-primary-600' : 'text-amber-600']"
                  >
                    ${{ formatCurrency(currentMonthSummary.balance) }}
                  </dd>
                </dl>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- AI Insights Panel -->
    <div class="space-y-4">
      <h2 class="text-lg font-bold text-gray-700 font-sans">Financial Insights</h2>
      <InsightPanel />
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from 'pinia';
import { useTransactionStore } from '../stores/transactionStore';
import InsightPanel from '../components/InsightPanel.vue';

export default {
  name: 'DashboardView',
  components: {
    InsightPanel,
  },
  computed: {
    ...mapState(useTransactionStore, ['transactions']),
    currentMonthName() {
      return new Date().toLocaleString('en-US', { month: 'long', year: 'numeric' });
    },
    currentMonthSummary() {
      const now = new Date();
      const currentYear = now.getFullYear();
      const currentMonth = now.getMonth(); // 0-indexed

      let totalIncome = 0;
      let totalExpense = 0;

      // Filter transactions belonging to current month & year
      const currentMonthTransactions = this.transactions.filter(t => {
        if (!t.date) return false;
        const txDate = new Date(t.date);
        return txDate.getFullYear() === currentYear && txDate.getMonth() === currentMonth;
      });

      currentMonthTransactions.forEach(t => {
        const amt = Number(t.amount) || 0;
        if (t.type === 'INCOME') {
          totalIncome += amt;
        } else {
          totalExpense += amt;
        }
      });

      return {
        totalIncome,
        totalExpense,
        balance: totalIncome - totalExpense,
      };
    },
  },
  mounted() {
    this.fetchAll();
  },
  methods: {
    ...mapActions(useTransactionStore, ['fetchAll']),
    formatCurrency(value) {
      if (value === undefined || value === null) return '0.00';
      return Number(value).toLocaleString('en-US', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
      });
    },
  },
};
</script>
