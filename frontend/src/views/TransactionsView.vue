<template>
  <div class="space-y-6 relative min-h-[500px]">
    <!-- Header -->
    <div class="flex items-center justify-between border-b border-gray-200 pb-5">
      <div>
        <h1 class="text-3xl font-extrabold text-gray-900 tracking-tight">Transactions</h1>
        <p class="mt-2 text-sm text-gray-500">Track and manage your income and expenses.</p>
      </div>
      <button
        @click="openAddForm"
        class="inline-flex items-center px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-semibold text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500 transition-colors"
      >
        <!-- plus icon -->
        <svg class="-ml-1 mr-2 h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
        </svg>
        Add Transaction
      </button>
    </div>

    <!-- Error/Loading indicators -->
    <div v-if="error" class="p-4 rounded-md bg-red-50 border border-red-200 text-sm text-red-600">
      {{ error }}
    </div>

    <!-- List Component -->
    <TransactionList
      :transactions="transactions"
      :summary="summary"
      @edit="handleEditRequest"
      @delete="handleDelete"
    />

    <!-- Slide-over Drawer Panel -->
    <div
      v-if="showForm"
      class="fixed inset-0 overflow-hidden z-50"
      aria-labelledby="slide-over-title"
      role="dialog"
      aria-modal="true"
    >
      <div class="absolute inset-0 overflow-hidden">
        <!-- Backdrop background overlay -->
        <div
          @click="closeForm"
          class="absolute inset-0 bg-gray-500 bg-opacity-75 transition-opacity"
          aria-hidden="true"
        ></div>

        <!-- Slide-over container -->
        <div class="fixed inset-y-0 right-0 pl-10 max-w-full flex">
          <div class="w-screen max-w-md">
            <div class="h-full flex flex-col bg-white shadow-xl overflow-y-scroll border-l border-gray-100">
              <div class="p-6">
                <!-- Form Component -->
                <TransactionForm
                  :initial-data="selectedTransaction"
                  @submitted="handleFormSubmit"
                  @cancel="closeForm"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from 'pinia';
import { useTransactionStore } from '../stores/transactionStore';
import TransactionList from '../components/TransactionList.vue';
import TransactionForm from '../components/TransactionForm.vue';

export default {
  name: 'TransactionsView',
  components: {
    TransactionList,
    TransactionForm,
  },
  data() {
    return {
      showForm: false,
      selectedTransaction: null,
    };
  },
  computed: {
    ...mapState(useTransactionStore, ['transactions', 'summary', 'isLoading', 'error']),
  },
  mounted() {
    this.fetchAll();
    this.fetchSummary();
  },
  methods: {
    ...mapActions(useTransactionStore, ['fetchAll', 'fetchSummary', 'create', 'update', 'remove']),
    openAddForm() {
      this.selectedTransaction = null;
      this.showForm = true;
    },
    closeForm() {
      this.showForm = false;
      this.selectedTransaction = null;
    },
    handleEditRequest(tx) {
      this.selectedTransaction = tx;
      this.showForm = true;
    },
    async handleFormSubmit(formData) {
      try {
        if (formData.id) {
          await this.update(formData.id, formData);
        } else {
          await this.create(formData);
        }
        this.closeForm();
      } catch (err) {
        // Error is captured in store state
      }
    },
    async handleDelete(id) {
      if (confirm('Are you sure you want to delete this transaction?')) {
        try {
          await this.remove(id);
        } catch (err) {
          // Error is captured in store state
        }
      }
    },
  },
};
</script>
