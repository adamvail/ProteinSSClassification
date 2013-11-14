function [ count ] = count_classed_correctly( w, y, Z )
%Returns the number of elements of y that are classified correctly

% Find all the y_i = -1 where w^T*z_i <= -1

count = numel( find((w.' * Z((y == -1), :).') <= -1) );

% Find all the y_i = 1 where w^T*z_i >= 1
count = count + numel( find((w.' * Z((y == 1), :).') >= 1) );

end

