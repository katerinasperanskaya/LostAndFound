const app = Vue.createApp({
  data() {
    return {
      // Dynamically initialize currentView based on authState
      currentView: authState.isLoggedIn
        ? (authState.role === "ADMIN" ? 'admin_users' : 'found_items')
        : 'login'
    };
  },

  methods: {
    changeView(newView) {
      if (["login", "register", "found_items", "lost_items", "admin_users"].includes(newView)) {
        this.currentView = newView;
      } else {
        console.warn(`🚨 Invalid view attempted: ${newView}`);
      }
    }
  }
,

  mounted() {
  }
});

// Reactive authState to manage login state globally
const authState = Vue.reactive({
  isLoggedIn: false,
  username: '',
  role: ''
});

// Check localStorage for persisted login state
const storedRole = localStorage.getItem('authRole');
const isLoggedIn = localStorage.getItem('isLoggedIn');

if (isLoggedIn === 'true') {
  authState.isLoggedIn = true;
  authState.role = storedRole;
  authState.username = localStorage.getItem('username') || ''; // Optional username
}

// Provide authState to all components
app.provide('authState', authState);

// List of all components to load
const componentFiles = [
  "navbar.js",                // Unrestricted
  "login.js",                 // Unrestricted
  "register.js",         	  
  "aside_menu.js",            // Unrestricted
  "admin/admin_dashboard.js", // Restricted - Admin
  "user/user_dashboard.js",   // Restricted - User
  "user/lost_items.js",       // Restricted - User
  "user/found_items.js",      // Restricted - User
  "admin/admin_claims.js",    // Restricted - Admin
  "admin/admin_users.js"      // Restricted - Admin
];

async function loadComponent(file) {
  try {
    const response = await fetch(`./components/${file}`);
    if (!response.ok) throw new Error(`Failed to load: ${file}`);

    const scriptText = await response.text();
    const blob = new Blob([scriptText], { type: 'application/javascript' });
    const blobUrl = URL.createObjectURL(blob);

    const module = await import(blobUrl);
    URL.revokeObjectURL(blobUrl);

    // Extract the component name from the file path
    const componentName = file.split('/').pop().replace(".js", "");
    app.component(componentName, module.default || module);
  } catch (error) {
    console.error(`Error loading component: ${file}`, error);
  }
}

async function loadComponents() {
  try {
    await Promise.all(componentFiles.map(loadComponent));
    console.log("All components loaded.");

    // Mount the app after all components are loaded
    app.mount("#app");
  } catch (error) {
    console.error("Error during component loading", error);
  }
}

loadComponents();