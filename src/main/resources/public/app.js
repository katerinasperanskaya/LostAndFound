const app = Vue.createApp({
  data() {
    return {
      currentView: 'login'  // Ensure login is the default view
    };
  },

  methods: {
    changeView(newView) {
      this.currentView = newView;
    }
  }
});

// 🔹 Ensure `authState` is available and not dependent on tokens
const authState = Vue.reactive({
  isLoggedIn: false,  // Authentication works, but no tokens
  username: ''
});

app.provide('authState', authState);

const unrestrictedComponents = [
  "navbar.js",
  "login.js",
  "aside_menu.js"  // 🔹 Keeping sidebar
];

const restrictedComponentFiles = {
  USER: [
    "user/dashboard.js",
    "user/lost_items.js",
    "user/found_items.js"
  ],
  ADMIN: [
    "admin/admin_dashboard.js",
    "admin/claims.js",
    "admin/users.js"
  ]
};

async function loadComponent(file) {
  try {
    const response = await fetch(`./components/${file}`);
    if (!response.ok) throw new Error(`Failed to load: ${file}`);
    
    const scriptText = await response.text();
    const blob = new Blob([scriptText], { type: 'application/javascript' });
    const blobUrl = URL.createObjectURL(blob);
    
    const module = await import(blobUrl);
    URL.revokeObjectURL(blobUrl);
    
	// Extract the component name from the file path.
	    const componentName = file.split('/').pop().replace(".js", "");
	    app.component(componentName, module.default || module);
	  } catch (error) {
	    console.error(`Error loading component: ${file}`, error);
	  }
}

async function loadComponents() {
  try {
    await Promise.all(unrestrictedComponents.map(loadComponent));
    console.log("✅ All components loaded.");
    
    app.mount("#app"); 
  } catch (error) {
    console.error("❌ Error during component loading", error);
  }
}

loadComponents();