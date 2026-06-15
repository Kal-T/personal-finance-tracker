<template>
  <form @submit.prevent="handleSubmit" class="space-y-6">
    <!-- Header -->
    <div class="border-b border-gray-200 pb-4">
      <h3 class="text-lg font-bold text-gray-900">
        {{ isEdit ? 'Edit Transaction' : 'New Transaction' }}
      </h3>
      <p class="text-sm text-gray-500">Enter details of the transaction below.</p>
    </div>

    <!-- Amount -->
    <div>
      <label for="amount" class="block text-sm font-semibold text-gray-700">Amount ($)</label>
      <div class="mt-1 relative rounded-md shadow-sm">
        <input
          id="amount"
          v-model.number="form.amount"
          type="number"
          step="0.01"
          placeholder="0.00"
          class="block w-full px-3 py-2 border rounded-md text-gray-900 focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
          :class="[errors.amount ? 'border-red-300 text-red-900 placeholder-red-300 focus:ring-red-500 focus:border-red-500' : 'border-gray-300']"
        />
      </div>
      <p v-if="errors.amount" class="mt-2 text-xs text-red-600">{{ errors.amount }}</p>
    </div>

    <!-- Type -->
    <div>
      <span class="block text-sm font-semibold text-gray-700">Type</span>
      <div class="mt-2 flex items-center space-x-6">
        <label class="flex items-center text-sm font-medium text-gray-900 cursor-pointer">
          <input
            v-model="form.type"
            type="radio"
            value="INCOME"
            class="h-4 w-4 text-primary-600 border-gray-300 focus:ring-primary-500"
          />
          <span class="ml-2 px-2.5 py-0.5 rounded-full text-xs font-semibold bg-green-100 text-green-800">
            Income
          </span>
        </label>
        <label class="flex items-center text-sm font-medium text-gray-900 cursor-pointer">
          <input
            v-model="form.type"
            type="radio"
            value="EXPENSE"
            class="h-4 w-4 text-red-600 border-gray-300 focus:ring-red-500"
          />
          <span class="ml-2 px-2.5 py-0.5 rounded-full text-xs font-semibold bg-red-100 text-red-800">
            Expense
          </span>
        </label>
      </div>
      <p v-if="errors.type" class="mt-2 text-xs text-red-600">{{ errors.type }}</p>
    </div>

    <!-- Category -->
    <div>
      <label for="category" class="block text-sm font-semibold text-gray-700">Category</label>
      <select
        id="category"
        v-model="form.category"
        class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md text-gray-900 focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
        :class="[errors.category ? 'border-red-300 text-red-900 focus:ring-red-500 focus:border-red-500' : 'border-gray-300']"
      >
        <option value="" disabled>Select a category</option>
        <option v-for="cat in categories" :key="cat" :value="cat">{{ cat }}</option>
      </select>
      <p v-if="errors.category" class="mt-2 text-xs text-red-600">{{ errors.category }}</p>
    </div>

    <!-- Date -->
    <div>
      <label for="date" class="block text-sm font-semibold text-gray-700">Date</label>
      <input
        id="date"
        v-model="form.date"
        type="date"
        class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md text-gray-900 focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
        :class="[errors.date ? 'border-red-300 text-red-900 focus:ring-red-500 focus:border-red-500' : 'border-gray-300']"
      />
      <p v-if="errors.date" class="mt-2 text-xs text-red-600">{{ errors.date }}</p>
    </div>

    <!-- Description -->
    <div>
      <label for="description" class="block text-sm font-semibold text-gray-700">Description (Optional)</label>
      <textarea
        id="description"
        v-model="form.description"
        rows="3"
        placeholder="Enter description..."
        class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md text-gray-900 focus:outline-none focus:ring-primary-500 focus:border-primary-500 sm:text-sm"
      ></textarea>
    </div>

    <!-- Submit / Cancel Actions -->
    <div class="flex items-center justify-end space-x-3 pt-4 border-t border-gray-200">
      <button
        type="button"
        @click="$emit('cancel')"
        class="px-4 py-2 border border-gray-300 rounded-md text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500"
      >
        Cancel
      </button>
      <button
        type="submit"
        class="px-4 py-2 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-primary-600 hover:bg-primary-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-500"
      >
        {{ isEdit ? 'Save Changes' : 'Add Transaction' }}
      </button>
    </div>
  </form>
</template>

<script>
export default {
  name: 'TransactionForm',
  props: {
    initialData: {
      type: Object,
      default: null,
    },
  },
  emits: ['submitted', 'cancel'],
  data() {
    return {
      form: {
        id: null,
        amount: '',
        category: '',
        type: 'EXPENSE',
        date: new Date().toISOString().substring(0, 10),
        description: '',
      },
      errors: {},
      categories: [
        'Food',
        'Salary',
        'Rent',
        'Utilities',
        'Entertainment',
        'Travel',
        'Shopping',
        'Other',
      ],
    };
  },
  computed: {
    isEdit() {
      return !!(this.initialData && this.initialData.id);
    },
  },
  watch: {
    initialData: {
      handler(newVal) {
        this.loadInitialData(newVal);
      },
      immediate: true,
      deep: true,
    },
  },
  methods: {
    loadInitialData(data) {
      if (data) {
        this.form = {
          id: data.id || null,
          amount: data.amount || '',
          category: data.category || '',
          type: data.type || 'EXPENSE',
          date: data.date || new Date().toISOString().substring(0, 10),
          description: data.description || '',
        };
      } else {
        this.resetForm();
      }
      this.errors = {};
    },
    resetForm() {
      this.form = {
        id: null,
        amount: '',
        category: '',
        type: 'EXPENSE',
        date: new Date().toISOString().substring(0, 10),
        description: '',
      };
    },
    validate() {
      const errors = {};
      if (this.form.amount === '' || this.form.amount === null || isNaN(this.form.amount)) {
        errors.amount = 'Amount is required and must be a number';
      } else if (Number(this.form.amount) <= 0) {
        errors.amount = 'Amount must be positive';
      }

      if (!this.form.category) {
        errors.category = 'Category is required';
      }

      if (!this.form.type) {
        errors.type = 'Type is required';
      }

      if (!this.form.date) {
        errors.date = 'Date is required';
      }

      this.errors = errors;
      return Object.keys(errors).length === 0;
    },
    handleSubmit() {
      if (this.validate()) {
        this.$emit('submitted', { ...this.form });
      }
    },
  },
};
</script>
