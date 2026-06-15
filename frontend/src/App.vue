<template>
  <div class="min-h-screen bg-gray-50 flex flex-col font-sans">
    <!-- Navigation Bar -->
    <nav class="bg-white border-b border-gray-200 sticky top-0 z-50">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex items-center">
            <!-- Brand Logo -->
            <div class="flex-shrink-0 flex items-center">
              <span class="text-xl font-extrabold text-primary-600 tracking-tight">FinanceTracker</span>
            </div>
            <!-- Links -->
            <div class="hidden sm:ml-8 sm:flex sm:space-x-8">
              <router-link
                to="/"
                class="inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium transition-colors duration-200"
                active-class="border-primary-500 text-gray-900"
                exact-active-class="border-primary-500 text-gray-900"
                :class="[$route.path !== '/' ? 'border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700' : '']"
              >
                Dashboard
              </router-link>
              <router-link
                to="/transactions"
                class="inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium transition-colors duration-200"
                active-class="border-primary-500 text-gray-900"
                exact-active-class="border-primary-500 text-gray-900"
                :class="[$route.path !== '/transactions' ? 'border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700' : '']"
              >
                Transactions
              </router-link>
              <router-link
                to="/chat"
                class="inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium transition-colors duration-200"
                active-class="border-primary-500 text-gray-900"
                exact-active-class="border-primary-500 text-gray-900"
                :class="[$route.path !== '/chat' ? 'border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700' : '']"
              >
                AI Assistant
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </nav>

    <!-- Main Content Area -->
    <main class="flex-grow max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <router-view />
    </main>
  </div>
</template>

<script>
export default {
  name: 'App',
};
</script>
