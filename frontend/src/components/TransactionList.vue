<template>
  <div class="space-y-6">
    <!-- Summary Cards -->
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
                <dt class="text-sm font-medium text-gray-500 truncate">Total Income</dt>
                <dd class="text-2xl font-bold text-green-600">${{ formatCurrency(summary.totalIncome) }}</dd>
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
                <dt class="text-sm font-medium text-gray-500 truncate">Total Expense</dt>
                <dd class="text-2xl font-bold text-red-600">${{ formatCurrency(summary.totalExpense) }}</dd>
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
              :class="[summary.balance >= 0 ? 'bg-primary-50' : 'bg-amber-50']"
            >
              <!-- Balance icon -->
              <svg
                class="h-6 w-6"
                :class="[summary.balance >= 0 ? 'text-primary-600' : 'text-amber-600']"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
            <div class="ml-5 w-0 flex-1">
              <dl>
                <dt class="text-sm font-medium text-gray-500 truncate">Net Balance</dt>
                <dd
                  class="text-2xl font-bold"
                  :class="[summary.balance >= 0 ? 'text-primary-600' : 'text-amber-600']"
                >
                  ${{ formatCurrency(summary.balance) }}
                </dd>
              </dl>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Table Card -->
    <div class="bg-white shadow rounded-lg border border-gray-100 overflow-hidden">
      <div class="px-6 py-4 border-b border-gray-200 flex items-center justify-between">
        <h3 class="text-lg font-bold text-gray-900">Transaction History</h3>
        <span class="text-xs text-gray-500">{{ transactions.length }} total transactions</span>
      </div>

      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th scope="col" class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Date</th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Category</th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Description</th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Type</th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Amount</th>
              <th scope="col" class="relative px-6 py-3 text-right text-xs font-semibold text-gray-500 uppercase tracking-wider">Actions</th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-if="transactions.length === 0">
              <td colspan="6" class="px-6 py-10 text-center text-sm text-gray-500">
                No transactions recorded yet. Click "Add Transaction" to start tracking.
              </td>
            </tr>
            <tr
              v-for="tx in transactions"
              :key="tx.id"
              class="transition-colors duration-150"
              :class="[tx.type === 'INCOME' ? 'hover:bg-green-50/20 bg-green-50/5' : 'hover:bg-red-50/20 bg-red-50/5']"
            >
              <!-- Date -->
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900 font-medium">
                {{ tx.date }}
              </td>
              <!-- Category -->
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold bg-gray-100 text-gray-800">
                  {{ tx.category }}
                </span>
              </td>
              <!-- Description -->
              <td class="px-6 py-4 text-sm text-gray-500 max-w-xs truncate">
                {{ tx.description || '—' }}
              </td>
              <!-- Type -->
              <td class="px-6 py-4 whitespace-nowrap text-sm">
                <span
                  class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold"
                  :class="[tx.type === 'INCOME' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800']"
                >
                  {{ tx.type === 'INCOME' ? 'Income' : 'Expense' }}
                </span>
              </td>
              <!-- Amount -->
              <td
                class="px-6 py-4 whitespace-nowrap text-sm font-bold"
                :class="[tx.type === 'INCOME' ? 'text-green-600' : 'text-red-600']"
              >
                {{ tx.type === 'INCOME' ? '+' : '-' }}${{ formatCurrency(tx.amount) }}
              </td>
              <!-- Actions -->
              <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium space-x-3">
                <button
                  @click="$emit('edit', tx)"
                  class="text-primary-600 hover:text-primary-900 font-semibold focus:outline-none"
                >
                  Edit
                </button>
                <button
                  @click="$emit('delete', tx.id)"
                  class="text-red-600 hover:text-red-900 font-semibold focus:outline-none"
                >
                  Delete
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'TransactionList',
  props: {
    transactions: {
      type: Array,
      required: true,
    },
    summary: {
      type: Object,
      required: true,
    },
  },
  emits: ['edit', 'delete'],
  methods: {
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
