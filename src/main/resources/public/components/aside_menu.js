//here is the codoe for it:

export default {
  template: `
    <div v-if="authState.isLoggedIn" 
         class="d-flex flex-column position-relative bg-white shadow-sm" 
         style="min-height: 100vh; border-radius: 0; 
                background: linear-gradient(180deg, #ffffff 0%, #f5f7fa 100%);">
        
        <!-- Toggle Button -->
        <div class="p-3 border-bottom">
            <button class="btn" 
                    @click="toggleSidebar" 
                    style="transition: transform 0.3s;">
                <i class="bi bi-list" 
                   :class="{ 'rotate-180': sidebarOpen }"
                   style="font-size: 1.25rem;"></i>
            </button>
        </div>

        <!-- Sidebar Content -->
        <aside :class="['flex-grow-1 overflow-hidden transition-width', 
                        sidebarOpen ? 'w-100' : 'w-20']"
               style="min-width: 70px; transition: all 0.3s cubic-bezier(0.65, 0, 0.35, 1);">
            
            <!-- Admin Panel!! -->
            <div v-if="authState.role === 'ADMIN'" 
                 class="d-flex flex-column p-3 h-100">
                
                <!-- User Info -->
                <div v-if="sidebarOpen" class="mb-4 text-center">
                    <img src="assets/images/logo.svg" 
                         alt="User" class="img-fluid rounded-circle mb-2" 
                         style="width: 60px; height: 60px;">
                    <h6 class="text-primary fw-bold mb-0">{{ authState.username }}</h6>
                    <small class="text-muted">Administrator</small>
                </div>

                <!-- Navigation Links -->
                <div class="list-group list-group-flush">
  <!--                  <button @click="changeView('admin_dashboard')"
                            :class="['list-group-item list-group-item-action border-0 rounded-pill mb-2',
                                     selectedView === 'admin_dashboard' ? 'active' : '']">
                        <i class="bi bi-speedometer2"></i>
                        <span v-if="sidebarOpen" class="m-3">Dashboard</span>
                    </button>-->

        <!--            <button @click="changeView('admin_claims')"
                            :class="['list-group-item list-group-item-action border-0 rounded-pill mb-2',
                                     selectedView === 'admin_claims' ? 'active' : '']">
                        <i class="bi bi-card-checklist"></i>
                        <span v-if="sidebarOpen" class="m-3">Claims</span>
                    </button> -->

                    <button @click="changeView('admin_users')"
                            :class="['list-group-item list-group-item-action border-0 rounded-pill mb-2',
                                     selectedView === 'admin_users' ? 'active' : '']">
                        <i class="bi bi-people"></i>
                        <span v-if="sidebarOpen" class="m-3">Users</span>
                    </button>
                </div>
            </div>

            <!-- User Panel -->
            <div v-if="authState.role === 'USER'" 
                 class="d-flex flex-column p-3 h-100">
                
                <!-- User Info -->
                <div v-if="sidebarOpen" class="mb-4 text-center">
                    <img src="assets/images/logo.svg" 
                         alt="User" class="img-fluid rounded-circle mb-2" 
                         style="width: 60px; height: 60px;">
                    <h6 class="text-primary fw-bold mb-0">{{ authState.username }}</h6>
                    <small class="text-muted">User</small>
                </div>

                <!-- Navigation Links -->
                <div class="list-group list-group-flush">
                  <!-- removing two side bar entries   <button @click="changeView('user_dashboard')"
                            :class="['list-group-item list-group-item-action border-0 rounded-pill mb-2',
                                     selectedView === 'user_dashboard' ? 'active' : '']">
                        <i class="bi bi-speedometer2"></i>
                        <span v-if="sidebarOpen" class="m-3">Dashboard</span>
                    </button>

                    <button @click="changeView('lost_items')"
                            :class="['list-group-item list-group-item-action border-0 rounded-pill mb-2',
                                     selectedView === 'lost_items' ? 'active' : '']">
                        <i class="bi bi-search"></i>
                        <span v-if="sidebarOpen" class="m-3">Lost Items</span>
                    </button>-->

                    <button @click="changeView('found_items')"
                            :class="['list-group-item list-group-item-action border-0 rounded-pill mb-2',
                                     selectedView === 'found_items' ? 'active' : '']">
                        <i class="bi bi-check-circle"></i>
                        <span v-if="sidebarOpen" class="m-3">Found Items</span>
                    </button> 
                </div>
            </div>
        </aside>
    </div>
  `,
  inject: ['authState'],
  data() {
    return {
      sidebarOpen: true,
      selectedView: '' // Track the active tab
    };
  },
  methods: {
    toggleSidebar() {
      this.sidebarOpen = !this.sidebarOpen;
    },
    changeView(view) {
      this.selectedView = view; // Update active tab
      this.$emit('change-view', view); // Emit event to parent component
    }
  }
};