output <- read.table(
   file = "../output/behaviors/output.txt",
   header = TRUE,
   stringsAsFactors = FALSE
   );

windows(width = 6, height = 5);
range <- 1:length(output$Time);
par(mar = c(4.5, 4.5, 1, 1));
plot(
   x = output$Time[range] / 3600, 
   y = output$downstreamFlow[range], 
   type = "l",
   lwd = 2,
   xlab = "Time (hr)",
   ylab = bquote(paste(
      "Discharge ( ",
      m^3, ~ s^-1,
      ")"
      ))
   );
lines(output$Time[range] / 3600, output$upstreamFlow, lt="dashed", lwd = 2);

windows(width = 6, height = 5);
par(mar = c(4.5, 4.5, 1, 1));
plot(
   x = output$Time / 3600, 
   y = output$downstreamDO, 
   type = "l",
   lwd = 2,
   ylim = c(
      min(output$downstreamDO, output$upstreamDO),
      max(output$downstreamDO, output$upstreamDO)
      ),
   xlab = "Time (hr)",
   ylab = bquote(paste(
      "DO conc. (g ",
      ~ m^-3,
      ")"
      ))
   );
lines(output$Time / 3600, output$upstreamDO, lty="dashed", lwd = 2);

