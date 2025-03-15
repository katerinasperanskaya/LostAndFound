export default {
  template: `
  <div class="container mt-5">
     <h1>Found Items</h1>
     <!-- Add Found Item Button -->
     <button @click="showAddItemForm = true" class="btn btn-success mb-3">Add Found Item</button>
     <!-- Add Found Item Form -->
     <div v-if="showAddItemForm" class="card p-3 mb-3">
        <h4>Add a New Found Item</h4>
        <form @submit.prevent="addFoundItem">
          <div class="mb-2">
            <label class="form-label">Title</label>
            <input v-model="newItem.title" type="text" class="form-control" required>
          </div>
          <div class="mb-2">
            <label class="form-label">Description</label>
            <textarea v-model="newItem.description" class="form-control" required></textarea>
          </div>
          <div class="mb-2">
            <label class="form-label">Category</label>
            <input v-model="newItem.category" type="text" class="form-control" required>
          </div>
          <div class="mb-2">
            <label class="form-label">Location Found</label>
            <input v-model="newItem.locationFound" type="text" class="form-control" required>
          </div>
          <div class="mb-2">
            <label class="form-label">Date Found</label>
            <input v-model="newItem.dateFound" type="datetime-local" class="form-control" required>
          </div>
          <button type="submit" class="btn btn-primary">Submit</button>
          <button type="button" @click="showAddItemForm = false" class="btn btn-secondary ms-2">Cancel</button>
        </form>
     </div>
     <!-- Category Filter Dropdown -->
     <div class="mb-3">
        <label for="category" class="form-label">Filter by Category</label>
        <select v-model="selectedCategory" class="form-select" id="category" @change="filterItems">
            <option value="">All Categories</option>
            <option v-for="category in categories" :key="category" :value="category">
                {{ category }}
            </option>
        </select>
     </div>
     <!-- Found Items Table -->
     <div v-if="filteredItems.length">
        <table class="table table-striped table-bordered mt-3">
            <thead class="table-primary">
                <tr>
                    <th>Title</th>
                    <th>Description</th>
                    <th>Category</th>
                    <th>Location Found</th>
                    <th>Date Found</th>
                    <th>Status</th>
                    <th>Claim item</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="item in filteredItems" :key="item.id">
                    <td>{{ item.title }}</td>
                    <td>{{ item.description }}</td>
                    <td>{{ item.category }}</td>
                    <td>{{ item.locationFound }}</td>
                    <td>{{ formatDate(item.dateFound) }}</td>
                    <td>
                        <span :class="{'badge bg-success': item.status === 'CLAIMED', 'badge bg-warning': item.status === 'UNCLAIMED'}">
                            {{ item.status }}
                        </span>
                    </td>
                    <td>
                        <button v-if="item.status === 'UNCLAIMED'" @click="handleClaim(item.id, item.userId)" class="btn btn-primary btn-sm">
                            Claim
                        </button>
                        <span v-else class="text-success">Claimed</span>
                    </td>
                </tr>
            </tbody>
        </table>
     </div>
     <!-- No Items Found Message -->
     <p v-else class="text-muted text-center">No found items available.</p>
  </div>
     <!-- Contact Info Modal -->
     <div class="modal fade" id="contactModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Contact Information</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <p><strong>Name:</strong> {{ contactInfo.name }}</p>
                    <p><strong>Email:</strong> {{ contactInfo.email }}</p>
                    <p><strong>Phone:</strong> {{ contactInfo.phone }}</p>
                    <p class="text-muted">Please contact the person to claim your item.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                </div>
            </div>
        </div>
     </div>
  </div>
  `,
  data() {
    return {
      showAddItemForm: false,
      selectedCategory: "",
      categories: [],
      foundItems: [],
      filteredItems: [],
      newItem: {
        userId: 1, // Hardcoded userId, replace with actual logged-in user ID
        title: "",
        description: "",
        category: "",
        locationFound: "",
        dateFound: "",
        status: "UNCLAIMED"
      },
      contactInfo: {
        name: "",
        email: "",
        phone: ""
      }
    };
  },
  async created() {
    await this.fetchFoundItems();
  },
  methods: {
    // Helper function to get the JWT token from localStorage
    getAuthToken() {
      const token = localStorage.getItem('authToken');
      if (!token) {
        console.error("No JWT token found in localStorage");
        throw new Error("No JWT token available");
      }
      return token;
    },

    async fetchFoundItems() {
      try {
        const token = this.getAuthToken();
        const response = await fetch("http://localhost:9091/api/found-items", {
          method: "GET",
          headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
          }
        });
        if (!response.ok) {
          throw new Error(`Failed to fetch found items: ${response.statusText}`);
        }
        this.foundItems = await response.json();
        this.categories = [...new Set(this.foundItems.map(item => item.category))];
        this.filteredItems = this.foundItems;
      } catch (error) {
        console.error("Error fetching found items:", error.message);
      }
    },

    async openContactModal(userId) {
      try {
        const token = this.getAuthToken();
        const response = await fetch(`http://localhost:9091/api/users/${userId}`, {
          method: "GET",
          headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
          }
        });
        if (!response.ok) {
          throw new Error(`Failed to fetch contact info: ${response.statusText}`);
        }
        const user = await response.json();
        this.contactInfo = {
          name: user.name || "Unknown",
          email: user.email || "No email provided",
          phone: user.phone || "No phone number available"
        };
        const modal = new bootstrap.Modal(document.getElementById("contactModal"));
        modal.show();
      } catch (error) {
        console.error("Error fetching contact info:", error.message);
      }
    },

    async handleClaim(itemId, userId) {
      // Call both functions: update claim status and show modal
      await this.claimItem(itemId);
      this.openContactModal(userId);
    },

    async addFoundItem() {
      try {
        const token = this.getAuthToken();
        const response = await fetch("http://localhost:9091/api/found-items", {
          method: "POST",
          headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
          },
          body: JSON.stringify(this.newItem)
        });
        if (response.ok) {
          const addedItem = await response.json();
          this.foundItems.push(addedItem);
          this.filterItems();
          this.showAddItemForm = false;
        } else {
          console.error("Failed to add found item");
        }
      } catch (error) {
        console.error("Error adding found item:", error.message);
      }
    },

    filterItems() {
      this.filteredItems = this.selectedCategory
        ? this.foundItems.filter(item => item.category === this.selectedCategory)
        : this.foundItems;
    },

    async claimItem(itemId) {
      try {
        const token = this.getAuthToken();
        const response = await fetch(`http://localhost:9091/api/found-items/${itemId}/status/CLAIMED`, {
          method: "PATCH",
          headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json"
          }
        });
        if (response.ok) {
          this.foundItems = this.foundItems.map(item =>
            item.id === itemId ? { ...item, status: "CLAIMED" } : item
          );
          this.filterItems();
        } else {
          console.error("Failed to update status.");
        }
      } catch (error) {
        console.error("Error claiming item:", error.message);
      }
    },

    formatDate(dateString) {
      return new Date(dateString).toLocaleDateString();
    }
  }
};