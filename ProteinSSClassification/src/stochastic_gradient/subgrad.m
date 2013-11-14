function [ subgrad_value ] = subgrad( mu, w, y_i_k, z_i_k)
%Subgradient of support vector classification function for HW 8
%   Returns the value of a subgradient of the function at the given point.


subgrad_value = mu * w;

if (y_i_k * w.' * z_i_k < 1)
    
    subgrad_value = subgrad_value - y_i_k * z_i_k;
    
end


end

