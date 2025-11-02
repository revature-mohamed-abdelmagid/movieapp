function formatDuration(minutes) {
  if (!minutes || isNaN(minutes)) {
    return 'N/A'; // Handle invalid input gracefully
  }
  
  const hours = Math.floor(minutes / 60);
  const mins = minutes % 60;
  
  if (mins === 0) {
    return `${hours}h`;
  } else {
    return `${hours}h ${mins}m`;
  }
}

export { formatDuration };