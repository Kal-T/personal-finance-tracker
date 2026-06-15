<template>
  <div class="space-y-6">
    <h1 class="text-3xl font-extrabold text-gray-900 tracking-tight">Transactions</h1>
    <p class="text-gray-600">Manage your transactions here.</p>
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <div class="md:col-span-1">
        <TransactionForm />
      </div>
      <div class="md:col-span-2">
        <TransactionList />
      </div>
    </div>
  </div>
</template>

<script>
import TransactionForm from '../components/TransactionForm.vue';
import TransactionList from '../components/TransactionList.vue';

export default {
  name: 'TransactionsView',
  components: {
    TransactionForm,
    TransactionList
  }
}
</script>
