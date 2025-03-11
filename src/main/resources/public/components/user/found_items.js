export default {
  template: `
  <div class="container mt-5">
     <h1>Found Items</h1>

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
                    <th>Image</th>
                    <th>Claim</th>
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
                        <img :src="item.imageUrl" alt="Found Item Image" class="img-thumbnail" width="80">
                    </td>
                    <td>
                        <button v-if="item.status === 'UNCLAIMED'" @click="claimItem(item.id)" class="btn btn-primary btn-sm">
                            It's Mine!
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
  `,
  data() {
    return {
      selectedCategory: "",
      categories: [],
      foundItems: [],
      filteredItems: []
    };
  },
  async created() {
    try {
      const response = await fetch("http://localhost:9091/api/found-items");
      this.foundItems = await response.json();

      // Extract unique categories from found items
      this.categories = [...new Set(this.foundItems.map(item => item.category))];

      // Show all found items by default
      this.filteredItems = this.foundItems;
    } catch (error) {
      console.error("Error fetching found items:", error);
    }
  },
  methods: {
    filterItems() {
      // If no category is selected, show all found items
      this.filteredItems = this.selectedCategory
        ? this.foundItems.filter(item => item.category === this.selectedCategory)
        : this.foundItems;
    },
    async claimItem(itemId) {
      try {
        const response = await fetch(`http://localhost:9091/api/found-items/${itemId}/status/CLAIMED`, {
          method: "PATCH"
        });

        if (response.ok) {
          // Update status in UI after successful claim
          this.foundItems = this.foundItems.map(item =>
            item.id === itemId ? { ...item, status: "CLAIMED" } : item
          );
          this.filterItems(); // Refresh the filtered list
        } else {
          console.error("Failed to update status.");
        }
      } catch (error) {
        console.error("Error claiming item:", error);
      }
    },
    formatDate(dateString) {
      return new Date(dateString).toLocaleDateString();
    }
  }
};
