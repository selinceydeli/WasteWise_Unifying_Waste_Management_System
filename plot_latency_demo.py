import pandas as pd
import matplotlib.pyplot as plt
import os

results_dir = "sharding_experiment_results"
output_file = f"{results_dir}/latency_comparison_line_chart.png"
csv_file = f"{results_dir}/latency_demo_results.csv"

# Delete the existing file if it exists
if os.path.exists(output_file):
    os.remove(output_file)

# Load CSV data
data = pd.read_csv(csv_file)
average_latency = data.groupby(['country', 'collectionName'])['averageLatency'].mean().unstack()

label_mapping = {
    'listingCollection': 'Sharded Listing Collection',
    'nonShardedCollection': 'Non-sharded Listing Collection'
}

# Plot a line chart
plt.figure(figsize=(12, 8))
for collection in average_latency.columns:
    plt.plot(average_latency.index, average_latency[collection], marker='o', label=label_mapping[collection])

    for i, value in enumerate(average_latency[collection]):
        plt.text(i, value, f"{value:.2f}", ha="center", va="bottom")

plt.title("Average Latency for High-Frequency Queries (Sharded vs Non-Sharded)")
plt.xlabel("Country")
plt.ylabel("Average Latency (ms)")
plt.xticks(rotation=45)
plt.legend(title="Collection Type")
plt.tight_layout()

# Save and display plot
plt.savefig(output_file)
plt.show()
