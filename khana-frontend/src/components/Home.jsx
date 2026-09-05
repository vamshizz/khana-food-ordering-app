import { Link } from "react-router-dom";
import { useSelector } from "react-redux";
import { useEffect, useState } from "react";

export default function Home() {
    const token = useSelector((state) => state.user.user.token);  // ✅
    const email = useSelector((state) => state.user.user.email);  // ✅

    const [rest, setRest] = useState([]);

    useEffect(() => {
        fetch("http://localhost:8072/api/orders/food/restuarant", {
            method: "GET",
            headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": "application/json",
            },
        })
        .then((res) => {
            if (!res.ok) throw new Error("Failed to fetch data");
            return res.json();
        })
        .then((data) => {
            setRest(data);
        })
        .catch((error) => {
            console.error("Error fetching data:", error);
        });
    }, [token]);

    return (
        <div className="Homedit">
            {rest && (
                <div className="hotel">
                    {rest.map((restaurant) => (
                        <div key={restaurant.restaurantId} className="design">
                            <Link to={`/menu/${restaurant.restaurantId}`}>
                                <h3 className="restname">{restaurant.restaurantName}</h3>
                                <img
                                    src={"http://localhost:8080/images/" + restaurant.restaurantLogo}
                                    alt={restaurant.restaurantName}
                                    className="restimage"
                                />
                            </Link>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}