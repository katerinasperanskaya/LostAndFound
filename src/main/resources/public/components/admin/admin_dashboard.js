export default {
  template: `
<div class="container mt-5">
    <h1>Admin Dashboard</h1>
    <canvas id="statusChart"></canvas>
</div>
  `,
  data() {
    return {
      statusData: { CLAIMED: 0, UNCLAIMED: 0 },
      chartInstance: null, // Store chart instance
    };
  },
  mounted() {
    this.fetchStatusData();
  },
  methods: {
    async fetchStatusData() {
      try {
        // Retrieve the JWT token from localStorage
        const token = localStorage.getItem('authToken');

        // Check if the token exists
        if (!token) {
          console.error("No JWT token found in localStorage");
          return;
        }

        // Fetch unclaimed data with the Authorization header
        const unclaimedRes = await fetch("http://localhost:9091/api/found-items/status/UNCLAIMED", {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`, // Include the token
            'Content-Type': 'application/json'
          }
        });

        // Fetch claimed data with the Authorization header
        const claimedRes = await fetch("http://localhost:9091/api/found-items/status/CLAIMED", {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`, // Include the token
            'Content-Type': 'application/json'
          }
        });

        // Handle non-2xx responses
        if (!unclaimedRes.ok || !claimedRes.ok) {
          throw new Error(`Failed to fetch status data: ${unclaimedRes.statusText || claimedRes.statusText}`);
        }

        const unclaimedData = await unclaimedRes.json();
        const claimedData = await claimedRes.json();

        console.log("Unclaimed Data:", unclaimedData);
        console.log("Claimed Data:", claimedData);

        // Ensure only whole numbers
        this.statusData.UNCLAIMED = Math.floor(unclaimedData.length);
        this.statusData.CLAIMED = Math.floor(claimedData.length);

        this.renderChart();
      } catch (error) {
        console.error("Error fetching status data:", error.message);
      }
    },
    renderChart() {
      const ctx = document.getElementById("statusChart").getContext("2d");

      // Destroy existing chart instance if it exists to avoid duplicate charts
      if (this.chartInstance) {
        this.chartInstance.destroy();
      }

      // Create a new chart
      this.chartInstance = new Chart(ctx, {
        type: "bar",
        data: {
          labels: ["Unclaimed", "Claimed"],
          datasets: [
            {
              label: "Found Items Status",
              data: [this.statusData.UNCLAIMED, this.statusData.CLAIMED],
              backgroundColor: ["#A379C9", "#9AD5CA"]
            }
          ]
        },
        options: {
          responsive: true,
          scales: {
            y: {
              beginAtZero: true,
              ticks: {
                stepSize: 1, // Only whole numbers
                callback: function (value) {
                  return Number.isInteger(value) ? value : null;
                }
              }
            }
          }
        }
      });
    }
  }
};